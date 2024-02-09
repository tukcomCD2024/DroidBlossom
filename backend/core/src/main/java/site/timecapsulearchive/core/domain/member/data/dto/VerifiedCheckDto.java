package site.timecapsulearchive.core.domain.member.data.dto;

public record VerifiedCheckDto(
    Long memberId,
    Boolean isVerified
) {

}
