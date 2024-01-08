package site.timecapsulearchive.core.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

@Schema(description = "회원 요약 정보")
@Validated
public record MemberSummaryResponse(
    @JsonProperty("id")
    @Schema(description = "회원 아이디")
    Long id,

    @JsonProperty("nickname")
    @Schema(description = "회원 닉네임")
    String nickname,

    @JsonProperty("profileUrl")
    @Schema(description = "회원 프로필 url")
    String profileUrl
) {

}