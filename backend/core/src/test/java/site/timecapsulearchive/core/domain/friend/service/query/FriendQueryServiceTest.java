package site.timecapsulearchive.core.domain.friend.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Slice;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.dto.FriendDtoFixture;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDtoByTag;
import site.timecapsulearchive.core.domain.friend.data.request.FriendBeforeGroupInviteRequest;
import site.timecapsulearchive.core.domain.friend.exception.FriendNotFoundException;
import site.timecapsulearchive.core.domain.friend.repository.friend_invite.FriendInviteRepository;
import site.timecapsulearchive.core.domain.friend.repository.member_friend.MemberFriendRepository;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.domain.member_group.repository.group_invite_repository.GroupInviteRepository;
import site.timecapsulearchive.core.domain.member_group.repository.member_group_repository.MemberGroupRepository;
import site.timecapsulearchive.core.global.common.wrapper.ByteArrayWrapper;

class FriendQueryServiceTest {

    private final MemberRepository memberRepository = mock(
        MemberRepository.class);
    private final MemberFriendRepository memberFriendRepository = mock(
        MemberFriendRepository.class);
    private final MemberGroupRepository memberGroupRepository = mock(MemberGroupRepository.class);
    private final GroupInviteRepository groupInviteRepository = mock(GroupInviteRepository.class);
    private final FriendInviteRepository friendInviteRepository = mock(
        FriendInviteRepository.class);

    private final FriendQueryService friendQueryService = new FriendQueryService(
        memberRepository,
        memberFriendRepository,
        memberGroupRepository,
        groupInviteRepository,
        friendInviteRepository
    );

    @Test
    void 사용자는_주소록_기반_핸드폰_번호로_ARchive_사용자_리스트를_조회_할_수_있다() {
        //given
        Long memberId = 1L;
        int startFriendsIndex = 0;
        List<ByteArrayWrapper> phones = MemberFixture.getPhones(5);

        given(memberRepository.findMemberPhoneHash(anyLong())).willReturn(
            Optional.of(MemberFixture.getPhoneByteWrapper(startFriendsIndex)));
        given(memberFriendRepository.findFriendsByPhone(anyLong(), anyList()))
            .willReturn(FriendDtoFixture.getFriendSummaryDtos(5));

        //when
        List<SearchFriendSummaryDto> dtos = friendQueryService.findFriendsByPhone(memberId, phones);

        //then
        assertThat(dtos).isNotEmpty();
    }


    @Test
    void 사용자는_주소록_기반_핸드폰_번호_없이_ARchive_사용자_리스트_조회하면_빈_리스트를_반환한다() {
        //given
        Long memberId = 1L;
        int startFriendsIndex = 0;
        List<ByteArrayWrapper> phones = Collections.emptyList();

        given(memberRepository.findMemberPhoneHash(anyLong())).willReturn(
            Optional.of(MemberFixture.getPhoneByteWrapper(startFriendsIndex)));
        given(memberFriendRepository.findFriendsByPhone(anyLong(), anyList()))
            .willReturn(Collections.emptyList());

        //when
        List<SearchFriendSummaryDto> dtos = friendQueryService.findFriendsByPhone(memberId,
            phones);

        //then
        assertTrue(dtos.isEmpty());
    }

    @Test
    void 사용자는_주소록_기반_번호로_ARchive_사용자를_검색하면_본인은_조회되지_않는다() {
        //given
        Long memberId = 1L;
        int friendsCount = 5;
        int startFriendsIndex = 0;

        List<ByteArrayWrapper> phones = MemberFixture.getPhones(friendsCount);

        given(memberRepository.findMemberPhoneHash(anyLong())).willReturn(
            Optional.of(MemberFixture.getPhoneByteWrapper(startFriendsIndex)));

        given(memberFriendRepository.findFriendsByPhone(anyLong(), anyList()))
            .willReturn(FriendDtoFixture.getFriendSummaryDtos(friendsCount));

        List<SearchFriendSummaryDto> dtos = friendQueryService.findFriendsByPhone(memberId,
            phones);

        assertThat(dtos.size()).isEqualTo(friendsCount - 1);
    }

    @Test
    void 사용자는_태그로_ARchive_사용자를_검색할_수_있다() {
        //given
        Long memberId = 1L;
        String tag = "testTag";
        Optional<SearchFriendSummaryDtoByTag> summaryDtoByTag = FriendDtoFixture.getFriendSummaryDtoByTag();
        SearchFriendSummaryDtoByTag expectDto = summaryDtoByTag.get();

        given(memberFriendRepository.findFriendsByTag(anyLong(), anyString()))
            .willReturn(summaryDtoByTag);

        //when
        SearchFriendSummaryDtoByTag actualResponse = friendQueryService.searchFriend(
            memberId, tag);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(expectDto.id()).isEqualTo(actualResponse.id());
            softly.assertThat(expectDto.profileUrl()).isEqualTo(actualResponse.profileUrl());
            softly.assertThat(expectDto.nickname()).isEqualTo(actualResponse.nickname());
            softly.assertThat(expectDto.isFriend()).isEqualTo(actualResponse.isFriend());
        });
    }

    @Test
    void 사용자는_존재하지_않는_태그로_ARchive_사용자를_검색하면_예외가_발생한다() {
        //given
        Long memberId = 1L;
        String tag = "testTag";

        given(memberFriendRepository.findFriendsByTag(anyLong(), anyString()))
            .willReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> friendQueryService.searchFriend(memberId, tag))
            .isInstanceOf(FriendNotFoundException.class);
    }

    @Test
    void 그룹장은_그룹_초대_전_초대_가능한_친구_목록을_조회할_수_있다() {
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(1);

        FriendBeforeGroupInviteRequest request = FriendBeforeGroupInviteRequest.of(memberId,
            groupId,
            size, now);
        given(memberFriendRepository.findFriends(request)).willReturn(
            FriendDtoFixture.getFriendSummaryDtoSlice(5, true));
        given(memberGroupRepository.findGroupMemberIdsByGroupId(request.groupId())).willReturn(
            List.of(3L));
        given(groupInviteRepository.findGroupMemberIdsByGroupIdAndGroupOwnerId(request.groupId(),
            request.memberId())).willReturn(List.of(4L));

        Slice<FriendSummaryDto> friendsBeforeGroupInviteSlice = friendQueryService.findFriendsBeforeGroupInviteSlice(
            request);

        SoftAssertions.assertSoftly(
            softly -> {
                assertThat(friendsBeforeGroupInviteSlice.getContent()).isNotEmpty();
                assertThat(friendsBeforeGroupInviteSlice.getContent()).allMatch(
                    dto -> !dto.profileUrl().isBlank());
                assertThat(friendsBeforeGroupInviteSlice.getContent()).allMatch(
                    dto -> !dto.nickname().isBlank());
                assertThat(friendsBeforeGroupInviteSlice.getContent()).allMatch(
                    dto -> Objects.nonNull(dto.id()));
                assertThat(friendsBeforeGroupInviteSlice.getContent()).allMatch(
                    dto -> Objects.nonNull(dto.createdAt()));
                assertThat(friendsBeforeGroupInviteSlice.hasNext()).isTrue();
                assertThat(friendsBeforeGroupInviteSlice.getSize()).isEqualTo(5);
            }
        );
    }

    @Test
    void 그룹장은_그룹_초대_전_이미_그룹멤버_혹은_그룹_요청을_보낸_사용자를_제외하고_초대_가능한_사용자를_조회한다() {
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(1);

        FriendBeforeGroupInviteRequest request = FriendBeforeGroupInviteRequest.of(memberId,
            groupId,
            size, now);
        given(memberFriendRepository.findFriends(request)).willReturn(
            FriendDtoFixture.getFriendSummaryDtoSlice(5, true));
        given(memberGroupRepository.findGroupMemberIdsByGroupId(request.groupId())).willReturn(
            List.of(3L));
        given(groupInviteRepository.findGroupMemberIdsByGroupIdAndGroupOwnerId(request.groupId(),
            request.memberId())).willReturn(List.of(4L));

        Slice<FriendSummaryDto> friendsBeforeGroupInviteSlice = friendQueryService.findFriendsBeforeGroupInviteSlice(
            request);

        assertThat(friendsBeforeGroupInviteSlice.getContent()).isNotIn(3L, 4L);
    }
}