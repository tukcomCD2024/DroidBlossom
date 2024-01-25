package site.timecapsulearchive.core.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 상세 정보")
public record MemberDetailResponse(

    @Schema(description = "닉네임")
    String nickname,

    @Schema(description = "프로필 url")
    String profileUrl,

    @Schema(description = "핸드폰 번호")
    String phone
) {

}