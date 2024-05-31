package site.timecapsulearchive.core.domain.group.data.dto;

import java.util.List;
import java.util.function.Function;
import site.timecapsulearchive.core.domain.group.data.response.GroupMemberInfoResponse;

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
