package site.timecapsulearchive.core.domain.group.data.dto;

import java.util.function.Function;
import lombok.Builder;
import site.timecapsulearchive.core.domain.group.data.response.GroupMemberWithRelationResponse;

@Builder
public record GroupMemberWithRelationDto(
    Long memberId,
    String profileUrl,
    String nickname,
    String tag,
    Boolean isOwner,
    Boolean isFriend
) {

    public GroupMemberWithRelationResponse toResponse(
        final Function<String, String> singlePreSignUrlFunction) {
        return GroupMemberWithRelationResponse.builder()
            .memberId(memberId)
            .profileUrl(singlePreSignUrlFunction.apply(profileUrl))
            .nickname(nickname)
            .tag(tag)
            .isOwner(isOwner)
            .isFriend(isFriend)
            .build();
    }
}
