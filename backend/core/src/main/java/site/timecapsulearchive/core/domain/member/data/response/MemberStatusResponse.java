package site.timecapsulearchive.core.domain.member.data.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 상태 확인 응답")
public record MemberStatusResponse(

    @Schema(description = "사용자 존재 여부")
    Boolean isExist,

    @Schema(description = "전화번호 인증 여부")
    Boolean isVerified,

    @Schema(description = "회원 탈퇴 여부")
    Boolean isDeleted
) {

}
