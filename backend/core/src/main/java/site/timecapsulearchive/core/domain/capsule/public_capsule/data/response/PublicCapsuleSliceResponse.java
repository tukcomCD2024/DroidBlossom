package site.timecapsulearchive.core.domain.capsule.public_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.CapsuleDetailResponse;

@Schema(description = "공개 캡슐 슬라이싱")
public record PublicCapsuleSliceResponse(

    @Schema(description = "공개 캡슐 정보")
    List<CapsuleDetailResponse> publicCapsules,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext
) {

}
