package site.timecapsulearchive.core.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

@Schema(description = "회원 상세 정보")
@Validated
public record MemberDetailResponse(
    @JsonProperty("nickname")
    @Schema(description = "닉네임")
    String nickname,

    @JsonProperty("profileUrl")
    @Schema(description = "프로필 url")
    String profileUrl,

    @JsonProperty("phone")
    @Schema(description = "핸드폰 번호")
    String phone
) {

}