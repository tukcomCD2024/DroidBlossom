package site.timecapsulearchive.core.domain.auth.data.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description = "이메일 로그인 요청")
public record EmailSignInRequest(

    @Schema(description = "이메일")
    @Email
    String email,

    @Schema(description = "비밀번호")
    String password
) {

}
