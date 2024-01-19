package site.timecapsulearchive.core.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인증 번호로 인증 요청")
public record VerificationMessageValidRequest(

    @Schema(description = "인증 번호")
    Long number
) {

}
