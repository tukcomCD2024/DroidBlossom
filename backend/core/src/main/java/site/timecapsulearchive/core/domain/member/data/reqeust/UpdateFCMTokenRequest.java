package site.timecapsulearchive.core.domain.member.data.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "FCM 토큰 업데이트 포맷")
public record UpdateFCMTokenRequest(

    @Schema(description = "FCM 토큰")
    @NotNull(message = "FCM 토큰은 필수입니다.")
    String fcmToken
) {

}
