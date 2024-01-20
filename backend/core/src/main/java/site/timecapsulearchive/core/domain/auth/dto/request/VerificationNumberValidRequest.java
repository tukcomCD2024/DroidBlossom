package site.timecapsulearchive.core.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "인증 번호로 인증 요청")
public record VerificationNumberValidRequest(

    @Schema(description = "인증 번호")
    @NotNull(message = "인증번호는 필수 입니다.")
    Integer certificationNumber
) {

}
