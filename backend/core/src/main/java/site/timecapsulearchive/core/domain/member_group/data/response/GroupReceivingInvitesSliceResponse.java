package site.timecapsulearchive.core.domain.member_group.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.Function;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupInviteSummaryDto;

public record GroupReceivingInvitesSliceResponse(
    @Schema(description = "초대 온 그룹 요약 정보 리스트")
    List<GroupReceivingInviteSummaryResponse> responses,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext
) {

    public static GroupReceivingInvitesSliceResponse createOf(
        final List<GroupInviteSummaryDto> dtos,
        final boolean hasNext,
        final Function<String, String> preSignedUrlFunction
    ) {
        List<GroupReceivingInviteSummaryResponse> groupReceivingInviteSummaryResponse = dtos.stream()
            .map(dto -> dto.toResponse(preSignedUrlFunction)).toList();

        return new GroupReceivingInvitesSliceResponse(groupReceivingInviteSummaryResponse,
            hasNext);
    }
}
