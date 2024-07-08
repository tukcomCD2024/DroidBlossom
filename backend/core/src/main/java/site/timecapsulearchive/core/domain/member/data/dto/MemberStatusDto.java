package site.timecapsulearchive.core.domain.member.data.dto;

import site.timecapsulearchive.core.domain.member.data.response.MemberStatusResponse;

public record MemberStatusDto(
    Boolean isExist,
    Boolean isVerified,
    Boolean isDeleted
) {

    public static MemberStatusDto exist(boolean isVerified, boolean deletedAt) {
        return new MemberStatusDto(true, isVerified, deletedAt);
    }

    public static MemberStatusDto notExist() {
        return new MemberStatusDto(false, false, false);
    }

    public MemberStatusResponse toResponse() {
        return new MemberStatusResponse(isExist, isVerified, isDeleted);
    }
}
