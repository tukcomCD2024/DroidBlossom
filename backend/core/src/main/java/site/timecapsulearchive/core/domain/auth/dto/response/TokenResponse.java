package site.timecapsulearchive.core.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

@Schema(description = "완전한 인증 토큰")
@Validated
public record TokenResponse(
    @JsonProperty("accessToken")
    @Schema(description = "액세스 토큰")
    String accessToken,

    @JsonProperty("refreshToken")
    @Schema(description = "리프레시 토큰")
    String refreshToken
) {

}