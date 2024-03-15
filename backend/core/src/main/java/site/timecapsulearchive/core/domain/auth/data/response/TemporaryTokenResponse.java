package site.timecapsulearchive.core.domain.auth.data.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "임시 인증 토큰")
public record TemporaryTokenResponse(

    @Schema(description = "임시 액세스 토큰")
    String temporaryAccessToken,

    @Schema(description = "임시 액세스 토큰 만료 시간")
    long expiresIn
) {

    public static TemporaryTokenResponse create(
        String temporaryAccessToken,
        long expiresIn
    ) {
        return new TemporaryTokenResponse(
            temporaryAccessToken,
            expiresIn
        );
    }
}
