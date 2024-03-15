package site.timecapsulearchive.core.domain.member.data.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 요약 정보")
public record MemberSummaryResponse(

    @Schema(description = "회원 아이디")
    Long id,

    @Schema(description = "회원 닉네임")
    String nickname,

    @Schema(description = "회원 프로필 url")
    String profileUrl
) {

}