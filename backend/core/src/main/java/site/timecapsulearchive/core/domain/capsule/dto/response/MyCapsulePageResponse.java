package site.timecapsulearchive.core.domain.capsule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.SecretCapsuleDetailDto;

@Schema(description = "내가 생성한 캡슐 페이징")
public record MyCapsulePageResponse(

    @Schema(description = "캡슐 상세 정보 리스트")
    List<SecretCapsuleDetailDto> capsules,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext
) {

}
