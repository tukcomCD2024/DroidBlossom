package site.timecapsulearchive.notification.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CapsuleSkinNotificationSendRequest(

    @NotNull
    Long memberId,

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
