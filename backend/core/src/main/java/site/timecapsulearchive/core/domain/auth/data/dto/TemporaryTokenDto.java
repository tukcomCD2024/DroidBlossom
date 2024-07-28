package site.timecapsulearchive.core.domain.auth.data.dto;

import site.timecapsulearchive.core.domain.auth.data.response.TemporaryTokenResponse;

public record TemporaryTokenDto(
    String temporaryAccessToken,
    long expiresIn
) {

    public static TemporaryTokenDto create(
        final String temporaryAccessToken,
        final long expiresIn
    ) {
        return new TemporaryTokenDto(
            temporaryAccessToken,
            expiresIn
        );
    }

    public TemporaryTokenResponse toResponse() {
        return new TemporaryTokenResponse(temporaryAccessToken, expiresIn);
    }
}
