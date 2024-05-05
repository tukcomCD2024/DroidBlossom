package site.timecapsulearchive.notification.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import site.timecapsulearchive.notification.entity.NotificationStatus;

public record CapsuleSkinNotificationSendRequest(

    @NotNull(message = "멤버 아이디는 필수 입니다.")
    Long memberId,

    @NotNull(message = "알림 상태는 필수 입니다.")
    NotificationStatus status,

    @NotBlank(message = "스킨 이름은 필수 입니다.")
    String skinName,

    @NotBlank(message = "알림 내용은 필수 입니다.")
    String title,

    @NotBlank(message = "알림 내용은 필수 입니다.")
    String text,

    @NotBlank(message = "스킨 URL은 필수 입니다.")
    String skinUrl
) {

}
