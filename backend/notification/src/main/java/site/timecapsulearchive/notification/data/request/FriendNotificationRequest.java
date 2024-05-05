package site.timecapsulearchive.notification.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import site.timecapsulearchive.notification.entity.NotificationStatus;

public record FriendNotificationRequest(

    @NotNull(message = "멤버 아이디는 필수 입니다.")
    Long targetId,

    @NotNull(message = "알림 상태는 필수 입니다.")
    NotificationStatus status,

    @NotBlank(message = "알림 제목은 필수 입니다.")
    String text,

    @NotBlank(message = "알림 내용은 필수 입니다.")
    String title
) {

}
