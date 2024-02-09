package site.timecapsulearchive.core.domain.member.data.dto;

public record MemberDetailResponseDto(
    String nickname,
    String profileUrl,
    byte[] phone
) {

}
