package site.timecapsulearchive.core.domain.member.data.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "알림 설정 업데이트 포맷")
public record UpdateNotificationEnabledRequest(

    @Schema(description = "알림 수신 유무")
    @NotNull
    Boolean notificationEnabled
) {

}
