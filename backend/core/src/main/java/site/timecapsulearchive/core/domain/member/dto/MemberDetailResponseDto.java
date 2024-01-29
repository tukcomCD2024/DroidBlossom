package site.timecapsulearchive.core.domain.member.dto;

public record MemberDetailResponseDto(
    String nickname,
    String profileUrl,
    String phone
) {

}
