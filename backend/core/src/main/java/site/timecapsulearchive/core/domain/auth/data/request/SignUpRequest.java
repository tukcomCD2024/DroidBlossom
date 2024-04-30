package site.timecapsulearchive.core.domain.auth.data.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import site.timecapsulearchive.core.domain.member.data.dto.SignUpRequestDto;
import site.timecapsulearchive.core.domain.member.entity.SocialType;

@Schema(description = "소셜 프로바이더의 인증 아이디로 회원가입 요청")
public record SignUpRequest(

    @Schema(description = "소셜 프로바이더 인증 아이디")
    @NotBlank(message = "인증 아이디는 필수입니다.")
    String authId,

    @Schema(description = "사용자 이메일")
    @NotNull(message = "사용자 이메일은 필수입니다.")
    @Email
    String email,

    @Schema(description = "사용자 프로필 url")
    @NotNull(message = "사용자 프로필 url은 필수입니다.")
    String profileUrl,

    @Schema(description = "소셜 프로바이더 타입")
    @NotNull(message = "소셜 프로바이더 타입은 필수입니다.")
    SocialType socialType
) {

    public SignUpRequestDto toDto() {
        return new SignUpRequestDto(
            authId,
            email,
            profileUrl,
            socialType
        );
    }

}
