package site.timecapsulearchive.core.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

@Schema(description = "임시 인증 토큰")
@Validated
public record TokenReIssueRequest(

    @Schema(description = "리프레시 토큰")
    String refreshToken
) {

}
