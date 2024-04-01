package site.timecapsulearchive.core.common.fixture;

import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.member.entity.Member;

public class MemberFriendFixture {

    public static MemberFriend friend(Member owner, Member friend) {
        return MemberFriend.builder()
            .owner(owner)
            .friend(friend)
    }

    public static MemberFriend memberFriend(Member owner, Member friend) {
        return MemberFriend.builder()
            .friend(friend)
            .owner(owner)
            .build();
    }
}
