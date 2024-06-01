package site.timecapsulearchive.core.domain.member_group.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupSendingInviteMemberDto;

@Schema(description = "그룹 초대 보낸 목록")
public record GroupSendingInvitesSliceResponse(
    @Schema(description = "초대 보낸 그룹원 정보 리스트")
    List<GroupSendingInviteMemberResponse> responses,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext
) {

    public static GroupSendingInvitesSliceResponse createOf(
        final List<GroupSendingInviteMemberDto> groupSendingInviteMemberDtos,
        final boolean hasNext
    ) {
        List<GroupSendingInviteMemberResponse> groupSendingInviteMemberResponses = groupSendingInviteMemberDtos.stream()
            .map(GroupSendingInviteMemberDto::toResponse)
            .toList();

        return new GroupSendingInvitesSliceResponse(groupSendingInviteMemberResponses, hasNext);
    }
}
