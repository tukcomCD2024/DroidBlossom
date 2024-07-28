package site.timecapsulearchive.core.domain.auth.data.dto;

import site.timecapsulearchive.core.domain.auth.data.response.VerificationMessageSendResponse;

public record VerificationMessageSendDto(
    Integer status,
    String message
) {

    public static VerificationMessageSendDto success(final Integer status,
        final String message) {
        return new VerificationMessageSendDto(status, message);
    }

    public VerificationMessageSendResponse toResponse() {
        return new VerificationMessageSendResponse(status, message);
    }
}
