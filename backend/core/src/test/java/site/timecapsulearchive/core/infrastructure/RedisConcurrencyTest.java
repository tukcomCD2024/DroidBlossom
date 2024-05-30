package site.timecapsulearchive.core.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import site.timecapsulearchive.core.common.RedissonTest;
import site.timecapsulearchive.core.common.dependency.TestTransactionTemplate;
import site.timecapsulearchive.core.common.fixture.domain.GroupFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.domain.group.repository.GroupRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.domain.member_group.repository.groupInviteRepository.GroupInviteRepository;
import site.timecapsulearchive.core.domain.member_group.repository.memberGroupRepository.MemberGroupRepository;
import site.timecapsulearchive.core.domain.member_group.service.MemberGroupCommandService;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.RedisLockException;
import site.timecapsulearchive.core.infra.queue.manager.SocialNotificationManager;

@TestConstructor(autowireMode = AutowireMode.ALL)
class RedisConcurrencyTest extends RedissonTest {

    private static final int MAX_THREADS_COUNT = 10;

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final GroupRepository groupRepository = mock(GroupRepository.class);
    private final MemberGroupRepository memberGroupRepository = mock(MemberGroupRepository.class);
    private final GroupInviteRepository groupInviteRepository = mock(GroupInviteRepository.class);

    private final MemberGroupCommandService groupMemberCommandService = spy(
        new MemberGroupCommandService(
            memberRepository,
            groupRepository,
            memberGroupRepository,
            groupInviteRepository,
            TestTransactionTemplate.spied(),
            mock(SocialNotificationManager.class))
    );


    @Test
    void 사용자는_그룹_초대_요청을_수락할_때_레디스_분산락을_통해_동기적으로_처리한다() throws InterruptedException {
        //given
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS_COUNT);
        CountDownLatch latch = new CountDownLatch(MAX_THREADS_COUNT);
        AtomicInteger countCheck = new AtomicInteger(MAX_THREADS_COUNT);

        Long memberId = 1L;
        Long groupId = 1L;

        Member groupMember = MemberFixture.member(1);

        given(groupRepository.getTotalGroupMemberCount(groupId))
            .willReturn(Optional.of(10L));
        given(memberRepository.findMemberById(memberId))
            .willReturn(Optional.of(groupMember));
        given(groupRepository.findGroupById(groupId)).willReturn(
            Optional.of(GroupFixture.group()));
        given(memberGroupRepository.findGroupOwnerId(groupId))
            .willReturn(Optional.of(2L));

        given(groupInviteRepository.deleteGroupInviteByGroupIdAndGroupOwnerIdAndGroupMemberId(
            groupId, 2L, memberId)).willReturn(1);

        //when
        for (int i = 0; i < MAX_THREADS_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    groupMemberCommandService.acceptGroupInvite(memberId, groupId);
                } finally {
                    latch.countDown();
                    int expectedCount = countCheck.decrementAndGet();
                    assertThat(expectedCount).isEqualTo(latch.getCount());
                }
            });
        }

        latch.await();
        executorService.shutdown();

        //then
        verify(groupInviteRepository,
            times(MAX_THREADS_COUNT)).deleteGroupInviteByGroupIdAndGroupOwnerIdAndGroupMemberId(
            anyLong(), anyLong(), anyLong());
        verify(memberGroupRepository, times(MAX_THREADS_COUNT)).save(any());
    }

    @Test
    void 사용자는_그룹_초대_요청을_수락할_때_락을_얻지_못하면_예외가_발생한다() {
        //given
        Long memberId = 1L;
        Long groupId = 1L;

        willThrow(new RedisLockException(ErrorCode.REDIS_FAILED_GET_LOCK_ERROR))
            .given(groupMemberCommandService).acceptGroupInvite(memberId, groupId);

        //when
        //then
        assertThatThrownBy(() -> groupMemberCommandService.acceptGroupInvite(memberId, groupId))
            .isInstanceOf(RedisLockException.class)
            .hasMessage(ErrorCode.REDIS_FAILED_GET_LOCK_ERROR.getMessage());
    }
}
