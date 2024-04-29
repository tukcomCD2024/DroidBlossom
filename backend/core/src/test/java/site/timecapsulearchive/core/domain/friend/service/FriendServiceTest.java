package site.timecapsulearchive.core.domain.friend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.common.fixture.MemberFixture;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.mapper.FriendMapper;
import site.timecapsulearchive.core.domain.friend.data.mapper.MemberFriendMapper;
import site.timecapsulearchive.core.domain.friend.repository.FriendInviteQueryRepository;
import site.timecapsulearchive.core.domain.friend.repository.FriendInviteRepository;
import site.timecapsulearchive.core.domain.friend.repository.MemberFriendQueryRepository;
import site.timecapsulearchive.core.domain.friend.repository.MemberFriendRepository;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.global.common.wrapper.ByteArrayWrapper;
import site.timecapsulearchive.core.global.security.encryption.AESEncryptionManager;
import site.timecapsulearchive.core.infra.notification.manager.NotificationManager;

class FriendServiceTest {

    private final MemberFriendMapper memberFriendMapper = new MemberFriendMapper(mock(
        AESEncryptionManager.class));
    private final MemberFriendQueryRepository memberFriendQueryRepository = mock(
        MemberFriendQueryRepository.class);
    private final MemberFriendRepository memberFriendRepository = mock(
        MemberFriendRepository.class);
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final FriendInviteRepository friendInviteRepository = mock(
        FriendInviteRepository.class);
    private final FriendInviteQueryRepository friendInviteQueryRepository = mock(
        FriendInviteQueryRepository.class);
    private final FriendMapper friendMapper = mock(FriendMapper.class);
    private final NotificationManager notificationManager = mock(NotificationManager.class);
    private final TransactionTemplate transactionTemplate = mock(TransactionTemplate.class);

    private final FriendService friendService = new FriendService(
        memberFriendRepository,
        memberFriendQueryRepository,
        memberFriendMapper,
        memberRepository,
        friendInviteRepository,
        friendInviteQueryRepository,
        friendMapper,
        notificationManager,
        transactionTemplate
    );

    @Test
    void 앱_사용자_핸드폰_번호로_주소록_기반_사용자_리스트_조회_테스트() {
        //given
        Long memberId = 1L;
        List<ByteArrayWrapper> phones = MemberFixture.getPhones();
        given(memberFriendQueryRepository.findFriendsByPhone(anyLong(), anyList()))
            .willReturn(getFriendSummaryDtos());

        //when
        List<SearchFriendSummaryDto> dtos = friendService.findFriendsByPhone(memberId, phones);

        //then
        assertThat(dtos.size()).isEqualTo(phones.size());
    }

    private List<SearchFriendSummaryDto> getFriendSummaryDtos() {
        List<SearchFriendSummaryDto> result = new ArrayList<>();
        for (long i = 0; i < 8; i++) {
            result.add(new SearchFriendSummaryDto(i, i + "testProfile.com", i + "testNickname",
                new ByteArrayWrapper(MemberFixture.getPhoneBytes((int) i)),
                Boolean.TRUE, Boolean.FALSE));
        }

        return result;
    }

    @Test
    void 번호_없이_주소록_기반_사용자_리스트_조회_테스트() {
        //given
        Long memberId = 1L;
        List<ByteArrayWrapper> phones = Collections.emptyList();
        given(memberFriendQueryRepository.findFriendsByPhone(anyLong(), anyList()))
            .willReturn(Collections.emptyList());

        //when
        List<SearchFriendSummaryDto> dtos = friendService.findFriendsByPhone(memberId,
            phones);

        //then
        assertThat(dtos.size()).isEqualTo(0);
    }
}