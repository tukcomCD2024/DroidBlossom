package site.timecapsulearchive.core.domain.member.data.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "태그 검색 허용 상태 변경 요청")
public record UpdateMemberTagSearchAvailableRequest(
    @Schema(description = "태그 검색 허용 상태 변경 요청 값")
    @NotNull
    Boolean tagSearchAvailable
) {

}
