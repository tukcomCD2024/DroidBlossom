package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleOpenStateResponse;

public record GroupCapsuleOpenStateDto(
    CapsuleOpenStatus capsuleOpenStatus
) {

    public static GroupCapsuleOpenStateDto opened() {
        return new GroupCapsuleOpenStateDto(CapsuleOpenStatus.OPEN);
    }

    public static GroupCapsuleOpenStateDto notOpened() {
        return new GroupCapsuleOpenStateDto(CapsuleOpenStatus.NOT_OPEN);
    }

    public GroupCapsuleOpenStateResponse toResponse() {
        return new GroupCapsuleOpenStateResponse(
            capsuleOpenStatus,
            capsuleOpenStatus.getStatusMessage()
        );
    }
}
