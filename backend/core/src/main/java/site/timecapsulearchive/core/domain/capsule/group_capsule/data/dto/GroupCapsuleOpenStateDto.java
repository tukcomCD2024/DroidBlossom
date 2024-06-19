package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleOpenStateResponse;

public record GroupCapsuleOpenStateDto(
    CapsuleOpenStatus capsuleOpenStatus,
    boolean isIndividuallyOpened
) {

    public static GroupCapsuleOpenStateDto opened() {
        return new GroupCapsuleOpenStateDto(CapsuleOpenStatus.OPEN, true);
    }

    public static GroupCapsuleOpenStateDto notOpened(boolean isIndividuallyOpened) {
        return new GroupCapsuleOpenStateDto(CapsuleOpenStatus.NOT_OPEN, isIndividuallyOpened);
    }

    public GroupCapsuleOpenStateResponse toResponse() {
        return new GroupCapsuleOpenStateResponse(
            capsuleOpenStatus,
            capsuleOpenStatus.getStatusMessage(),
            isIndividuallyOpened
        );
    }
}
