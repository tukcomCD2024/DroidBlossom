package site.timecapsulearchive.core.domain.capsule.dto.secret_c.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Schema(description = "비밀 캡슐 페이징")
@Validated
public record SecretCapsulePageResponse(

    @Schema(description = "캡슐 요약 정보 리스트")
    @Valid
    List<SecretCapsuleSummaryResponse> capsules,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext,

    @Schema(description = "이전 페이지 유무")
    Boolean hasPrevious
) {

}
