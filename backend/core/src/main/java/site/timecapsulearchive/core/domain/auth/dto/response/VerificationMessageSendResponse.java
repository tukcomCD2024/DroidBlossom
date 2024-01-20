package site.timecapsulearchive.core.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인증 문자 발송 응답")
public record VerificationMessageSendResponse(

    @Schema(description = "전송 상태")
    Integer status,

    @Schema(description = "상태 메시지")
    String message
) {

    public static VerificationMessageSendResponse success(final Integer status,
        final String message) {
        return new VerificationMessageSendResponse(status, message);
    }
}
