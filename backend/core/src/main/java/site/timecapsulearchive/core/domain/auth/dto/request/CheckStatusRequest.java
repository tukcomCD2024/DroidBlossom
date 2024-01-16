package site.timecapsulearchive.core.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import site.timecapsulearchive.core.domain.auth.entity.SocialType;

@Schema(description = "소셜 프로바이더의 인증 아이디로 회원 인증 상태 체크")
@Validated
public record CheckStatusRequest(

    @Schema(description = "소셜 프로바이더 인증 아이디")
    String authId,

    @Schema(description = "소셜 프로바이더 타입")
    SocialType socialType
) {

}
