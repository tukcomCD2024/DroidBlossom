package site.timecapsulearchive.core.domain.auth.data.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "완전한 인증 토큰")
public record TokenResponse(

    @Schema(description = "액세스 토큰")
    String accessToken,

    @Schema(description = "리프레시 토큰")
    String refreshToken,

    @Schema(description = "액세스 토큰 만료 시간")
    long expiresIn,

    @Schema(description = "리프레시 토큰 만료 시간")
    long refreshTokenExpiresIn
) {

}