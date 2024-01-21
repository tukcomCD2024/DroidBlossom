package site.timecapsulearchive.core.domain.member.dto;

public record MemberLoginDto(
    Long memberId,
    Boolean isVerified
) {

}
