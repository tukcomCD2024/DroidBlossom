package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import java.util.List;

public record GroupCapsuleWithMemberDetailDto(
    GroupCapsuleDetailDto groupCapsuleDetailDto,
    List<GroupCapsuleMemberSummaryDto> members
) {

    public GroupCapsuleWithMemberDetailDto excludeDetailContents() {
        return new GroupCapsuleWithMemberDetailDto(
            groupCapsuleDetailDto.excludeTitleAndContentAndImagesAndVideos(),
            members
        );
    }


}
