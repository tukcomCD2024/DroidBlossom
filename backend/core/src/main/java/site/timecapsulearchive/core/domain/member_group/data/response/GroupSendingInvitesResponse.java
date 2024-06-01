package site.timecapsulearchive.core.domain.member_group.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupSendingInviteMemberDto;

@Schema(description = "그룹 초대 보낸 목록")
public record GroupSendingInvitesResponse(

    @Schema(description = "초대 보낸 그룹원 정보 리스트")
    List<GroupSendingInviteMemberResponse> responses
) {

    public static GroupSendingInvitesResponse createOf(
        final List<GroupSendingInviteMemberDto> groupSendingInviteMemberDtos
    ) {
        List<GroupSendingInviteMemberResponse> groupSendingInviteMemberResponses = groupSendingInviteMemberDtos.stream()
            .map(GroupSendingInviteMemberDto::toResponse)
            .toList();

        return new GroupSendingInvitesResponse(groupSendingInviteMemberResponses);
    }
}
