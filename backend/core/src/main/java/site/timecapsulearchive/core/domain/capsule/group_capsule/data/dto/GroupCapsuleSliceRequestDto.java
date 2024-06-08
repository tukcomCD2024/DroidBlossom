package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

public record GroupCapsuleSliceRequestDto(
    Long memberId,
    Long groupId,
    int size,
    Long capsuleId
) {

    public static GroupCapsuleSliceRequestDto createOf(
        final Long memberId,
        final Long groupId,
        final int size,
        final Long capsuleId
    ) {
        return new GroupCapsuleSliceRequestDto(memberId, groupId, size, capsuleId);
    }
}
