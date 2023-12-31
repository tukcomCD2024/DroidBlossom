package site.timecapsulearchive.core.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

@Schema(description = "임시 인증 토큰")
@Validated
public record TemporaryTokenResponse(
    @JsonProperty("accessToken")
    @Schema(description = "액세스 토큰")
    String accessToken
) {

}
