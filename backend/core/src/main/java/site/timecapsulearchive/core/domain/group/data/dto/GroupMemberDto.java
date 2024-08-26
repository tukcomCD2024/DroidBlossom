package site.timecapsulearchive.core.domain.group.data.dto;

import java.util.function.Function;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendRelationDto;
import site.timecapsulearchive.core.domain.group.data.response.GroupMemberInfoResponse;

public record GroupMemberDto(
    Long memberId,
    String profileUrl,
    String nickname,
    String tag,
    Boolean isOwner
) {

    public GroupMemberWithRelationDto toRelationDto(final FriendRelationDto friendRelationDto) {
        return GroupMemberWithRelationDto.builder()
            .memberId(memberId)
            .profileUrl(profileUrl)
            .nickname(nickname)
            .tag(tag)
            .isOwner(isOwner)
            .isFriend(friendRelationDto.isFriend())
            .isFriendInviteToFriend(friendRelationDto.isFriendInviteToFriend())
            .isFriendInviteToMe(friendRelationDto.isFriendInviteToMe())
            .build();
    }

    public GroupMemberInfoResponse toInfoResponse(
        final Function<String, String> singlePreSignUrlFunction) {
        return GroupMemberInfoResponse.builder()
            .memberId(memberId)
            .profileUrl(singlePreSignUrlFunction.apply(profileUrl))
            .nickname(nickname)
            .tag(tag)
            .isOwner(isOwner)
            .build();
    }
}
