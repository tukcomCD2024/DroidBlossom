package site.timecapsulearchive.core.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

@Schema(description = "인증 문자 요청")
@Validated
public record VerificationMessageSendRequest(

    @Schema(description = "핸드폰 번호")
    String phone
) {

}