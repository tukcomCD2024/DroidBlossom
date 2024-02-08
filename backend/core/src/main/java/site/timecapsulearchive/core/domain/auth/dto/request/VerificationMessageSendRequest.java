package site.timecapsulearchive.core.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import site.timecapsulearchive.core.global.common.valid.annotation.Phone;

@Schema(description = "인증 문자 요청")
public record VerificationMessageSendRequest(

    @Schema(description = "수신자 핸드폰 번호 ex)01012341234", requiredMode = RequiredMode.REQUIRED)
    @Phone
    String receiver,

    @Schema(description = "앱의 해시 키")
    @NotBlank(message = "앱의 해시 키는 필수입니다.")
    String appHashKey
) {

}