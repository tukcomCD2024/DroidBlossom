package site.timecapsulearchive.notification.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import site.timecapsulearchive.notification.entity.CapsuleSkinCreationStatus;

public record CapsuleSkinNotificationSendRequest(

    @NotNull
    Long memberId,

    @NotNull
    CapsuleSkinCreationStatus status,

    @NotBlank
    String skinName,

    @NotBlank
    String title,

    @NotBlank
    String text,

    @NotBlank
    String skinUrl
) {

}
