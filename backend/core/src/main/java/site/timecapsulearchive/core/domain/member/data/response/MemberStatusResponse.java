package site.timecapsulearchive.core.domain.member.data.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 상태 확인 응답")
public record MemberStatusResponse(

    @Schema(description = "사용자 존재 여부")
    Boolean isExist,

    @Schema(description = "전화번호 인증 여부")
    Boolean isVerified
) {

    public static MemberStatusResponse empty() {
        return new MemberStatusResponse(false, false);
    }

    public static MemberStatusResponse from(Boolean isVerified) {
        return new MemberStatusResponse(true, isVerified);
    }
}
