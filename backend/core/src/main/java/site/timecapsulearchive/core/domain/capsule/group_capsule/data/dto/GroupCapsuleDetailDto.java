package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import java.util.List;
import java.util.function.Function;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleDetailResponse;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberSummaryDto;
import site.timecapsulearchive.core.domain.group.data.response.GroupMemberSummaryResponse;

public record GroupCapsuleDetailDto(
    CapsuleDetailDto capsuleDetailDto,
    List<GroupMemberSummaryDto> members
) {

    public List<GroupMemberSummaryResponse> groupMemberSummaryDtoToResponse() {
        return members.stream()
            .map(GroupMemberSummaryDto::toResponse)
            .toList();
    }

    public GroupCapsuleDetailDto excludeDetailContents() {
        return new GroupCapsuleDetailDto(
            capsuleDetailDto.excludeTitleAndContentAndImagesAndVideos(),
            members
        );
    }

    public GroupCapsuleDetailResponse toResponse(
        final Function<String, String> singlePreSignUrlFunction,
        final Function<String, List<String>> multiplePreSignUrlFunction,
        final Function<Point, Point> changePointFunction
    ) {
        final Point changePoint = changePointFunction.apply(capsuleDetailDto.point());

        final List<String> preSignedImageUrls = multiplePreSignUrlFunction.apply(
            capsuleDetailDto.images());
        final List<String> preSignedVideoUrls = multiplePreSignUrlFunction.apply(
            capsuleDetailDto.videos());

        return GroupCapsuleDetailResponse.builder()
            .capsuleId(capsuleDetailDto.capsuleId())
            .capsuleSkinUrl(singlePreSignUrlFunction.apply(capsuleDetailDto.capsuleSkinUrl()))
            .members(groupMemberSummaryDtoToResponse())
            .dueDate(capsuleDetailDto.dueDate())
            .nickname(capsuleDetailDto.nickname())
            .profileUrl(capsuleDetailDto.profileUrl())
            .createdDate(capsuleDetailDto.createdAt())
            .latitude(changePoint.getX())
            .longitude(changePoint.getY())
            .address(capsuleDetailDto.address())
            .roadName(capsuleDetailDto().roadName())
            .title(capsuleDetailDto().title())
            .content(capsuleDetailDto.content())
            .imageUrls(preSignedImageUrls)
            .videoUrls(preSignedVideoUrls)
            .isOpened(capsuleDetailDto.isOpened())
            .capsuleType(capsuleDetailDto.capsuleType())
            .build();
    }
}
