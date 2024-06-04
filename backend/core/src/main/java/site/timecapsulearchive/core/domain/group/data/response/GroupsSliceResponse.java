package site.timecapsulearchive.core.domain.group.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.Function;
import site.timecapsulearchive.core.domain.group.data.dto.CompleteGroupSummaryDto;

@Schema(description = "사용자의 그룹 목록 응답")
public record GroupsSliceResponse(

    @Schema(description = "그룹 목록")
    List<GroupSummaryResponse> groups,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext
) {

    public static GroupsSliceResponse createOf(
        final List<CompleteGroupSummaryDto> groups,
        final Boolean hasNext,
        final Function<String, String> preSignedUrlFunction
    ) {
        final List<GroupSummaryResponse> responses = groups.stream()
            .map(dto -> dto.toResponse(preSignedUrlFunction))
            .toList();

        return new GroupsSliceResponse(responses, hasNext);
    }
}
