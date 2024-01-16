package site.timecapsulearchive.core.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import site.timecapsulearchive.core.domain.auth.entity.SocialType;
import site.timecapsulearchive.core.domain.member.entity.Member;

@Schema(description = "소셜 프로바이더의 인증 아이디로 로그인 요청")
@Validated
public record SignInRequest(

    @Schema(description = "소셜 프로바이더 인증 아이디")
    String authId,

    @Schema(description = "사용자 이메일")
    String email,

    @Schema(description = "사용자 프로필 url")
    String profileUrl,

    @Schema(description = "소셜 프로바이더 타입")
    SocialType socialType
) {

    public Member toEntity() {
        return Member.builder()
            .authId(authId)
            .email(email)
            .profileUrl(profileUrl)
            .socialType(socialType)
            .build();
    }
}
