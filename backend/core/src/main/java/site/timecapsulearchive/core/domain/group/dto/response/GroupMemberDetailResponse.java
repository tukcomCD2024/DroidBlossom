package site.timecapsulearchive.core.domain.group.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

@Schema(description = "그룹원 상세 정보")
@Validated
public record GroupMemberDetailResponse(
    @JsonProperty("id")
    @Schema(description = "그룹원 아이디")
    Long id,

    @JsonProperty("nickname")
    @Schema(description = "그룹원 이름")
    String nickname,

    @JsonProperty("profileUrl")
    @Schema(description = "프로필 url")
    String profileUrl,

    @JsonProperty("isFriend")
    @Schema(description = "사용자와 친구 여부")
    Boolean isFriend
) {

}