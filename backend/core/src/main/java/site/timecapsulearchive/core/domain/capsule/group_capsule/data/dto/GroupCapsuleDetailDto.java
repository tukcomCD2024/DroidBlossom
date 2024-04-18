package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.List;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberSummaryDto;
import site.timecapsulearchive.core.domain.group.data.response.GroupMemberSummaryResponse;

public record GroupCapsuleDetailDto(
    Long capsuleId,
    String capsuleSkinUrl,
    List<GroupMemberSummaryDto> members,
    ZonedDateTime dueDate,
    String nickname,
    String profileUrl,
    ZonedDateTime createdAt,
    String address,
    String roadName,
    String title,
    String content,
    String images,
    String videos,
    Boolean isOpened,
    CapsuleType capsuleType
) {

    public GroupCapsuleDetailDto excludeTitleAndContentAndImagesAndVideos() {
        return new GroupCapsuleDetailDto(
            capsuleId,
            capsuleSkinUrl,
            members,
            dueDate,
            nickname,
            profileUrl,
            createdAt,
            address,
            roadName,
            "",
            "",
            "",
            "",
            isOpened,
            capsuleType
        );
    }

    public List<GroupMemberSummaryResponse> toGroupMemberSummaryResponse() {
        return members.stream()
            .map(GroupMemberSummaryDto::toResponse)
            .toList();
    }
}
