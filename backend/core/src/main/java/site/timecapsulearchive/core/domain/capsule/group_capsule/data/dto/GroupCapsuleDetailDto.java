package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleMemberSummaryResponse;

public record GroupCapsuleDetailDto(
    CapsuleDetailDto capsuleDetailDto,
    List<GroupCapsuleMemberSummaryDto> groupMembers
) {

    public List<GroupCapsuleMemberSummaryResponse> groupMemberSummaryDtoToResponse() {
        return groupMembers.stream()
            .map(GroupCapsuleMemberSummaryDto::toResponse)
            .toList();
    }

    public GroupCapsuleDetailDto excludeDetailContents() {
        return new GroupCapsuleDetailDto(
            capsuleDetailDto.excludeTitleAndContentAndImagesAndVideos(),
            groupMembers
        );
    }

    public GroupCapsuleDetailResponse toResponse(
        final UnaryOperator<String> singlePreSignUrlFunction,
        final Function<String, List<String>> multiplePreSignUrlFunction,
        final UnaryOperator<Point> pointTransformFunction
    ) {
        final Point changePoint = pointTransformFunction.apply(capsuleDetailDto.point());

        final List<String> preSignedImageUrls = multiplePreSignUrlFunction.apply(
            capsuleDetailDto.images());
        final List<String> preSignedVideoUrls = multiplePreSignUrlFunction.apply(
            capsuleDetailDto.videos());

        return GroupCapsuleDetailResponse.builder()
            .capsuleId(capsuleDetailDto.capsuleId())
            .capsuleSkinUrl(singlePreSignUrlFunction.apply(capsuleDetailDto.capsuleSkinUrl()))
            .groupMembers(groupMemberSummaryDtoToResponse())
            .dueDate(capsuleDetailDto.dueDate())
            .creatorNickname(capsuleDetailDto.nickname())
            .creatorProfileUrl(capsuleDetailDto.profileUrl())
            .createdAt(capsuleDetailDto.createdAt())
            .latitude(changePoint.getX())
            .longitude(changePoint.getY())
            .address(capsuleDetailDto.address())
            .roadName(capsuleDetailDto.roadName())
            .title(capsuleDetailDto.title())
            .content(capsuleDetailDto.content())
            .imageUrls(preSignedImageUrls)
            .videoUrls(preSignedVideoUrls)
            .isOpened(capsuleDetailDto.isOpened())
            .capsuleType(capsuleDetailDto.capsuleType())
            .build();
    }
}
