package site.timecapsulearchive.core.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import site.timecapsulearchive.core.global.common.valid.annotation.Phone;

@Schema(description = "인증 번호로 인증 요청")
public record VerificationNumberValidRequest(

    @Schema(description = "인증 번호")
    @NotNull(message = "인증번호는 필수 입니다.")
    String certificationNumber,

    @Schema(description = "수신자 핸드폰 번호 ex)01012341234", requiredMode = RequiredMode.REQUIRED)
    @Phone
    String receiver
) {

}
