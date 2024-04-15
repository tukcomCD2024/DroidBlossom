package site.timecapsulearchive.core.domain.friend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.PlatformTransactionManager;
import site.timecapsulearchive.core.common.fixture.MemberFixture;
import site.timecapsulearchive.core.domain.friend.data.dto.PhoneBook;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.mapper.FriendMapper;
import site.timecapsulearchive.core.domain.friend.data.mapper.MemberFriendMapper;
import site.timecapsulearchive.core.domain.friend.repository.FriendInviteQueryRepository;
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
    private final FriendInviteQueryRepository friendInviteQueryRepository = mock(
        FriendInviteQueryRepository.class);
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
        friendInviteQueryRepository,
        friendMapper,
        notificationManager,
        transactionTemplate,
        hashEncryptionManager
    );

    @Test
    void 앱_사용자_핸드폰_번호로_주소록_기반_사용자_리스트_조회_테스트() {
        //given
        Long memberId = 1L;
        Map<String, String> phones = getPhones();
        given(memberFriendQueryRepository.findFriendsByPhone(anyLong(), anySet()))
            .willReturn(getFriendSummaryDtos());

        //when
        Map<PhoneBook, SearchFriendSummaryDto> dtos = friendService.findFriendsByPhone(memberId,
            phones);

        //then
        assertThat(dtos.size()).isEqualTo(0);
    }

    private Map<String, String> getPhones() {
        return Map.of(
            "01012341234", "member1",
            "01012341235", "member2",
            "01012341236", "member3",
            "01012341237", "member4",
            "01012341238", "member5",
            "01012341239", "member6",
            "01012341240", "member7",
            "01012341241", "member8"
        );
    }

    private List<SearchFriendSummaryDto> getFriendSummaryDtos() {
        List<SearchFriendSummaryDto> result = new ArrayList<>();
        for (long i = 0; i < 8; i++) {
            result.add(new SearchFriendSummaryDto(i, i + "testProfile.com", i + "testNickname",
                MemberFixture.getPhoneBytes((int) i), MemberFixture.getPhoneBytes((int) i),
                Boolean.TRUE, Boolean.FALSE));
        }

        return result;
    }

    @Test
    void 번호_없이_주소록_기반_사용자_리스트_조회_테스트() {
        //given
        Long memberId = 1L;
        Map<String, String> phones = Collections.emptyMap();
        given(memberFriendQueryRepository.findFriendsByPhone(anyLong(), anySet()))
            .willReturn(Collections.emptyList());

        //when
        Map<PhoneBook, SearchFriendSummaryDto> dtos = friendService.findFriendsByPhone(memberId,
            phones);

        //then
        assertThat(dtos.size()).isEqualTo(0);
    }
}