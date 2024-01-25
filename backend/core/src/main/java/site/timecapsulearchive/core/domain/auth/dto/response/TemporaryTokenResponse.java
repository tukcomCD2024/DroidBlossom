package site.timecapsulearchive.core.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "임시 인증 토큰")
public record TemporaryTokenResponse(

    @Schema(description = "임시 액세스 토큰")
    String temporaryAccessToken,

    @Schema(description = "임시 액세스 토큰 만료 시간")
    String expiresIn
) {

    public static TemporaryTokenResponse create(
        String temporaryAccessToken,
        String expiresIn
    ) {
        return new TemporaryTokenResponse(
            temporaryAccessToken,
            expiresIn
        );
    }
}
