package site.timecapsulearchive.core.domain.member.data.dto;

public record EmailVerifiedCheckDto(
    Long memberId,
    Boolean isVerified,
    String email,
    String password
) {

}
