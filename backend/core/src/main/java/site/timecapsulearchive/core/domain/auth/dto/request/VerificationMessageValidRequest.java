package site.timecapsulearchive.core.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

@Schema(description = "인증 번호로 인증 요청")
@Validated
public record VerificationMessageValidRequest(
    @JsonProperty("number")
    @Schema(description = "인증 번호")
    Long number
) {

}
