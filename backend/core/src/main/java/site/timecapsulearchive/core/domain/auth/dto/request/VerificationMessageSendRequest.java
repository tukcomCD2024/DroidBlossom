package site.timecapsulearchive.core.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

@Schema(description = "인증 문자 요청")
@Validated
public record VerificationMessageSendRequest(
    @JsonProperty("phone")
    @Schema(description = "핸드폰 번호")
    String phone
) {

}