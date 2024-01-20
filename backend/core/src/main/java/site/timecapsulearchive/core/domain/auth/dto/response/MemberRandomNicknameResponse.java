package site.timecapsulearchive.core.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "랜덤된 닉네임")
public record MemberRandomNicknameResponse(

    @Schema(description = "사용자의 랜덤된 닉네임")
    String randomNickname

) {

}
