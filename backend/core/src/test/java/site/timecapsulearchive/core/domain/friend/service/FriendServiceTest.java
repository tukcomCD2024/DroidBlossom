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
import org.springframework.transaction.PlatformTransactionManager;
import site.timecapsulearchive.core.common.fixture.MemberFixture;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.mapper.FriendMapper;
import site.timecapsulearchive.core.domain.friend.data.mapper.MemberFriendMapper;
import site.timecapsulearchive.core.domain.friend.data.response.SearchFriendsResponse;
import site.timecapsulearchive.core.domain.friend.repository.FriendInviteRepository;
import site.timecapsulearchive.core.domain.friend.repository.MemberFriendQueryRepository;
import site.timecapsulearchive.core.domain.friend.repository.MemberFriendRepository;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.global.security.encryption.AESEncryptionManager;
import site.timecapsulearchive.core.global.security.encryption.HashEncryptionManager;
import site.timecapsulearchive.core.global.security.encryption.HashProperties;
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
    private final FriendMapper friendMapper = mock(FriendMapper.class);
    private final NotificationManager notificationManager = mock(NotificationManager.class);
    private final PlatformTransactionManager transactionTemplate = mock(
        PlatformTransactionManager.class);
    private final HashEncryptionManager hashEncryptionManager = new HashEncryptionManager(
        new HashProperties("test_salt"));

    private final FriendService friendService = new FriendService(
        memberFriendRepository,
        memberFriendQueryRepository,
        memberFriendMapper,
        memberRepository,
        friendInviteRepository,
        friendMapper,
        notificationManager,
        transactionTemplate,
        hashEncryptionManager
    );

    @Test
    void 앱_사용자_핸드폰_번호로_주소록_기반_사용자_리스트_조회_테스트() {
        //given
        Long memberId = 1L;
        List<String> phones = getPhones();
        given(memberFriendQueryRepository.findFriendsByPhone(anyLong(), anyList()))
            .willReturn(getFriendSummaryDtos());

        //when
        SearchFriendsResponse response = friendService.findFriendsByPhone(memberId, phones);

        //then
        assertThat(response.friends().size()).isEqualTo(phones.size());
    }

    private List<String> getPhones() {
        return List.of(
            "01012341234",
            "01012341235",
            "01012341236",
            "01012341237",
            "01012341238",
            "01012341239",
            "01012341240",
            "01012341241"
        );
    }

    private List<SearchFriendSummaryDto> getFriendSummaryDtos() {
        List<SearchFriendSummaryDto> result = new ArrayList<>();
        for (long i = 0; i < 8; i++) {
            result.add(new SearchFriendSummaryDto(i, i + "testProfile.com", i + "testNickname",
                MemberFixture.getPhoneBytes((int) i), Boolean.TRUE, Boolean.FALSE));
        }

        return result;
    }

    @Test
    void 번호_없이_주소록_기반_사용자_리스트_조회_테스트() {
        //given
        Long memberId = 1L;
        List<String> phones = Collections.emptyList();
        given(memberFriendQueryRepository.findFriendsByPhone(anyLong(), anyList()))
            .willReturn(Collections.emptyList());

        //when
        SearchFriendsResponse response = friendService.findFriendsByPhone(memberId, phones);

        //then
        assertThat(response.friends().size()).isEqualTo(0);
    }
}