package site.timecapsulearchive.core.domain.member.dto;

public record VerifiedCheckDto(
    Long memberId,
    Boolean isVerified
) {

}
