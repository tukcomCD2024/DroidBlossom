package site.timecapsulearchive.core.domain.member.data.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "이메일 중복 검증 요청")
public record CheckEmailDuplicationRequest(

    @Schema(description = "중복 검증할 이메일")
    @NotBlank
    @Email
    String email
) {

}
