package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleMemberSummaryResponse;

public record CombinedGroupCapsuleDetailDto(
    Long groupId,
    Long capsuleId,
    String capsuleSkinUrl,
    ZonedDateTime dueDate,
    Long creatorId,
    String nickname,
    String profileUrl,
    ZonedDateTime createdAt,
    Point point,
    String address,
    String roadName,
    String title,
    String content,
    String images,
    String videos,
    Boolean isCapsuleOpened,
    CapsuleType capsuleType,
    Boolean isRequestMemberCapsuleOpen,
    Boolean hasEditPermission,
    Boolean hasDeletePermission,
    List<GroupCapsuleMemberSummaryDto> groupMembers
) {

    public static CombinedGroupCapsuleDetailDto create(
        final GroupCapsuleDetailDto groupCapsuleDetailDto,
        final List<GroupCapsuleMemberSummaryDto> groupCapsuleMemberSummaryDtos,
        final Boolean isRequestMemberCapsuleOpen,
        final Boolean hasEditPermission,
        final Boolean hasDeletePermission
    ) {
        return new CombinedGroupCapsuleDetailDto(
            groupCapsuleDetailDto.groupId(),
            groupCapsuleDetailDto.capsuleId(),
            groupCapsuleDetailDto.capsuleSkinUrl(),
            groupCapsuleDetailDto.dueDate(),
            groupCapsuleDetailDto.creatorId(),
            groupCapsuleDetailDto.nickname(),
            groupCapsuleDetailDto.profileUrl(),
            groupCapsuleDetailDto.createdAt(),
            groupCapsuleDetailDto.point(),
            groupCapsuleDetailDto.address(),
            groupCapsuleDetailDto.roadName(),
            groupCapsuleDetailDto.title(),
            groupCapsuleDetailDto.content(),
            groupCapsuleDetailDto.images(),
            groupCapsuleDetailDto.videos(),
            groupCapsuleDetailDto.isCapsuleOpened(),
            groupCapsuleDetailDto.capsuleType(),
            isRequestMemberCapsuleOpen,
            hasEditPermission,
            hasDeletePermission,
            groupCapsuleMemberSummaryDtos
        );
    }

    public GroupCapsuleDetailResponse toResponse(
        final Function<String, List<String>> multiplePreSignUrlFunction,
        final UnaryOperator<String> preSignUrlFunction,
        final UnaryOperator<Point> changePointFunction
    ) {
        final Point changePoint = changePointFunction.apply(point);

        final List<GroupCapsuleMemberSummaryResponse> groupMembers = this.groupMembers.stream()
            .map(GroupCapsuleMemberSummaryDto::toResponse)
            .toList();

        return GroupCapsuleDetailResponse.builder()
            .groupId(groupId)
            .capsuleId(capsuleId)
            .capsuleSkinUrl(preSignUrlFunction.apply(capsuleSkinUrl))
            .members(groupMembers)
            .dueDate(dueDate)
            .nickname(nickname)
            .profileUrl(profileUrl)
            .createdDate(createdAt)
            .latitude(changePoint.getX())
            .longitude(changePoint.getY())
            .address(address)
            .roadName(roadName)
            .title(title)
            .content(content)
            .imageUrls(multiplePreSignUrlFunction.apply(images))
            .videoUrls(multiplePreSignUrlFunction.apply(videos))
            .isCapsuleOpened(isCapsuleOpened)
            .capsuleType(capsuleType)
            .isRequestMemberCapsuleOpened(isRequestMemberCapsuleOpen)
            .hasEditPermission(hasEditPermission)
            .hasDeletePermission(hasDeletePermission)
            .build();
    }

}
