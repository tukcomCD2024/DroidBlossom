package site.timecapsulearchive.core.domain.member.data.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import site.timecapsulearchive.core.global.common.valid.annotation.Phone;

@Schema(description = "사용자 핸드폰 번호 업데이트를 위한 인증번호 요청")
public record PhoneVerificationMessageRequest(
    @Schema(description = "핸드폰 번호")
    @Phone
    @NotNull(message = "사용자 핸드폰 번호는 필수 입니다.")
    String phone

) {

}
