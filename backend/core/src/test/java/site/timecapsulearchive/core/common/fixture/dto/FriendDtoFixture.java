package site.timecapsulearchive.core.common.fixture.dto;

import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDtoByTag;
import site.timecapsulearchive.core.global.common.wrapper.ByteArrayWrapper;

public class FriendDtoFixture {

    public static List<SearchFriendSummaryDto> getFriendSummaryDtos(int count) {
        return LongStream.range(0, count)
            .mapToObj(i -> SearchFriendSummaryDto.builder()
                .id(i)
                .profileUrl(i + "testProfile.com")
                .nickname(i + "testNickname")
                .phoneHash(new ByteArrayWrapper(MemberFixture.getPhoneBytes((int) i)))
                .isFriend(true)
                .isFriendInviteToFriend(false)
                .isFriendInviteToMe(false)
                .build())
            .toList();
    }

    public static Optional<SearchFriendSummaryDtoByTag> getFriendSummaryDtoByTag() {
        return Optional.of(SearchFriendSummaryDtoByTag.builder()
            .id(1L)
            .profileUrl("testProfile.com")
            .nickname("testNickname")
            .isFriend(true)
            .isFriendInviteToFriend(false)
            .isFriendInviteToMe(false)
            .build());

    }
}
