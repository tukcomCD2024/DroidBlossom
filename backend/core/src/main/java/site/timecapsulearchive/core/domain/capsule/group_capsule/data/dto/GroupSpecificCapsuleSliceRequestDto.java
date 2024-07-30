package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

public record GroupSpecificCapsuleSliceRequestDto(
    Long memberId,
    Long groupId,
    int size,
    Long lastCapsuleId
) {

    public static GroupSpecificCapsuleSliceRequestDto createOf(
        final Long memberId,
        final Long groupId,
        final int size,
        final Long lastCapsuleId
    ) {
        return new GroupSpecificCapsuleSliceRequestDto(memberId, groupId, size, lastCapsuleId);
    }
}
