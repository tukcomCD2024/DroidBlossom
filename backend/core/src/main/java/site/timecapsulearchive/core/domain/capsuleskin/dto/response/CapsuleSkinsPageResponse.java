package site.timecapsulearchive.core.domain.capsuleskin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Schema(description = "캡슐 스킨 페이징")
@Validated
public record CapsuleSkinsPageResponse(

    @Schema(description = "캡슐 스킨 요약 정보 리스트")
    @Valid
    List<CapsuleSkinSummaryResponse> skins,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext,

    @Schema(description = "이전 페이지 유무")
    Boolean hasPrevious
) {

}
