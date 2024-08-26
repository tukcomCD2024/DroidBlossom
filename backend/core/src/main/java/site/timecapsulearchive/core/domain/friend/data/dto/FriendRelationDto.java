package site.timecapsulearchive.core.domain.friend.data.dto;

public record FriendRelationDto(
    Boolean isFriend,
    Boolean isFriendInviteToFriend,
    Boolean isFriendInviteToMe
) {

}
