package site.timecapsulearchive.core.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "인증 문자 요청")
public record VerificationMessageSendRequest(

    @Schema(description = "수신자 핸드폰 번호 ex)01012341234")
    @Pattern(regexp = "^01(?:0|1|[6-9])?(\\d{3}|\\d{4})?(\\d{4})$", message = "숫자로만 구성된 전화번호 형식이여만합니다.")
    String receiver,

    @Schema(description = "앱의 해시 키")
    @NotBlank(message = "앱의 해시 키는 필수입니다.")
    String appHashKey
) {

}