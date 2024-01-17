package site.timecapsulearchive.core.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import site.timecapsulearchive.core.domain.auth.entity.SocialType;

@Schema(description = "소셜 프로바이더의 인증 아이디로 로그인 요청")
public record SignUpRequest(

    @Schema(description = "소셜 프로바이더 인증 아이디")
    @NotBlank
    String authId,

    @Schema(description = "사용자 이메일")
    @NotBlank
    @Email
    String email,

    @Schema(description = "사용자 프로필 url")
    @NotBlank
    String profileUrl,

    @Schema(description = "소셜 프로바이더 타입")
    @NotNull
    SocialType socialType
) {

}
