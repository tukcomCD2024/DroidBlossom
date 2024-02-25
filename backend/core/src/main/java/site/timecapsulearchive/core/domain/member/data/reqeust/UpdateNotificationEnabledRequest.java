package site.timecapsulearchive.core.domain.member.data.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "알림 설정 업데이트 포맷")
public record UpdateNotificationEnabledRequest(

    @Schema(description = "알림 수신 유무")
    @NotNull(message = "알림 상태는 필수 입니다.")
    Boolean notificationEnabled
) {

}
