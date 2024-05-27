package site.timecapsulearchive.core.domain.group.data.dto;

import java.util.List;

public record GroupMemberDto(
    Long memberId,
    String profileUrl,
    String nickname,
    String tag,
    Boolean isOwner
) {

    public GroupMemberWithRelationDto toRelationDto(final List<Long> friendIds) {
        return GroupMemberWithRelationDto.builder()
            .memberId(memberId)
            .profileUrl(profileUrl)
            .nickname(nickname)
            .tag(tag)
            .isOwner(isOwner)
            .isFriend(friendIds.contains(memberId))
            .build();
    }
}
