package site.timecapsulearchive.core.domain.member.dto.reqeust;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "회원 업데이트 포맷")
@Validated
public record MemberDetailUpdateRequest(
    @JsonProperty("nickname")
    @Schema(description = "닉네임")
    String nickname,

    @JsonProperty("profileImage")
    @Schema(description = "프로플 이미지")
    MultipartFile profileImage
) {

}