package site.timecapsulearchive.core.domain.friend.data.mapper;

import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;
import site.timecapsulearchive.core.domain.friend.entity.FriendStatus;
import site.timecapsulearchive.core.domain.member.entity.Member;

@Component
public class FriendMapper {

    public FriendInvite friendReqToEntity(Member owner, Member friend) {
        return FriendInvite.builder()
            .friendStatus(FriendStatus.PENDING)
            .owner(owner)
            .friend(friend)
            .build();
    }
}
