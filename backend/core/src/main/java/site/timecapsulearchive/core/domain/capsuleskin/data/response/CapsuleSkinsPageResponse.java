package site.timecapsulearchive.core.domain.capsuleskin.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "캡슐 스킨 페이징")
public record CapsuleSkinsPageResponse(

    @Schema(description = "캡슐 스킨 요약 정보 리스트")
    List<CapsuleSkinSummaryResponse> skins,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext
) {

}
