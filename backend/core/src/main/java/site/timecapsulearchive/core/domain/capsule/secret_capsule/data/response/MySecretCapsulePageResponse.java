package site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "내가 생성한 비밀 캡슐 목록 응답")
public record MySecretCapsulePageResponse(

    @Schema(description = "내 비밀 캡슐 리스트")
    List<MySecreteCapsuleResponse> capsules,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext
) {

}
