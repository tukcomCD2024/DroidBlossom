package site.timecapsulearchive.core.domain.auth.data.dto;

import site.timecapsulearchive.core.domain.auth.data.response.TokenResponse;

public record TokenDto(
    String accessToken,
    String refreshToken,
    long expiresIn,
    long refreshTokenExpiresIn
) {

    public static TokenDto create(
        String accessToken,
        String refreshToken,
        long expiresIn,
        long refreshTokenExpiresIn
    ) {
        return new TokenDto(
            accessToken,
            refreshToken,
            expiresIn,
            refreshTokenExpiresIn
        );
    }

    public TokenResponse toResponse() {
        return new TokenResponse(accessToken, refreshToken, expiresIn, refreshTokenExpiresIn);
    }
}
