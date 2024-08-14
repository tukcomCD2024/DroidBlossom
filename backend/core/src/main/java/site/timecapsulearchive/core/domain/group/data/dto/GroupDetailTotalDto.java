package site.timecapsulearchive.core.domain.group.data.dto;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendRelations;
import site.timecapsulearchive.core.domain.group.data.response.GroupDetailResponse;
import site.timecapsulearchive.core.domain.group.data.response.GroupMemberWithRelationResponse;

public record GroupDetailTotalDto(
    String groupName,
    String groupDescription,
    String groupProfileUrl,
    ZonedDateTime createdAt,
    Long groupCapsuleTotalCount,
    Boolean canGroupEdit,
    List<GroupMemberWithRelationDto> members
) {

    public static GroupDetailTotalDto as(
        final GroupDetailDto groupDetailDto,
        final Long groupCapsuleTotalCount,
        final FriendRelations friendRelations
    ) {
        List<GroupMemberWithRelationDto> membersWithRelation = Collections.emptyList();
        if (friendRelations != null) {
            membersWithRelation = groupDetailDto.members()
                .stream()
                .map(dto -> dto.toRelationDto(friendRelations.getFriendRelation(dto.memberId())))
                .toList();
        }

        return new GroupDetailTotalDto(
            groupDetailDto.groupName(),
            groupDetailDto.groupDescription(),
            groupDetailDto.groupProfileUrl(),
            groupDetailDto.createdAt(),
            groupCapsuleTotalCount,
            groupDetailDto.isOwner(),
            membersWithRelation
        );
    }

    public GroupDetailResponse toResponse(final Function<String, String> singlePreSignUrlFunction) {
        List<GroupMemberWithRelationResponse> groupMemberResponses = members.stream()
            .map(GroupMemberWithRelationDto::toResponse)
            .toList();

        return GroupDetailResponse.builder()
            .groupName(groupName)
            .groupDescription(groupDescription)
            .groupProfileUrl(singlePreSignUrlFunction.apply(groupProfileUrl))
            .createdAt(createdAt)
            .groupCapsuleTotalCount(groupCapsuleTotalCount)
            .canGroupEdit(canGroupEdit)
            .members(groupMemberResponses)
            .build();
    }
}
