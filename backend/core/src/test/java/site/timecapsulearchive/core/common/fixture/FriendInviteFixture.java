package site.timecapsulearchive.core.common.fixture;

import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;
import site.timecapsulearchive.core.domain.member.entity.Member;

public class FriendInviteFixture {

    public static FriendInvite friendInvite(Member owner, Member friend) {
        return FriendInvite.createOf(owner, friend);
    }
}
