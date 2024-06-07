package site.timecapsulearchive.core.domain.capsule.group_capsule.data.response;

import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.CapsuleOpenStatus;

public record GroupCapsuleOpenStateResponse(
    CapsuleOpenStatus capsuleOpenStatus,
    String statusMessage
) {

}
