package site.timecapsulearchive.core.domain.capsule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.response.MySecreteCapsuleResponse;

@Schema(description = "내가 생성한 비밀 캡슐 목록 응답")
public record MyCapsulePageResponse(

    @Schema(description = "내 비밀 캡슐 리스트")
    List<MySecreteCapsuleResponse> capsules,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext
) {

}
