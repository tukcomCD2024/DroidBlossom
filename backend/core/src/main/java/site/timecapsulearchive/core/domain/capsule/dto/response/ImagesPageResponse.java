package site.timecapsulearchive.core.domain.capsule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "캡슐 이미지 페이징")
public record ImagesPageResponse(

    @Schema(description = "이미지 요약 정보 리스트")
    List<ImageSummaryResponse> images,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext,

    @Schema(description = "이전 페이지 유무")
    Boolean hasPrevious
) {

}
