package site.timecapsulearchive.core.domain.group.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "그룹 요약 정보")
public record GroupSummaryResponse(

    @Schema(description = "그룹 아이디")
    Long id,

    @Schema(description = "그룹 이름")
    String name,

    @Schema(description = "그룹 프로필 url")
    String profileUrl,

    @Schema(description = "그룹 설명")
    String description
) {

}