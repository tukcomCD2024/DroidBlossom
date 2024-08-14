package site.timecapsulearchive.core.domain.friend.data.dto;

import java.util.Map;

public class FriendRelations {

    private final Map<Long, FriendRelationDto> friendRelationMap;

    public FriendRelations(Map<Long, FriendRelationDto> friendRelationMap) {
        this.friendRelationMap = friendRelationMap;
    }

    public FriendRelationDto getFriendRelation(final Long friendId) {
        return friendRelationMap.get(friendId);
    }
}
