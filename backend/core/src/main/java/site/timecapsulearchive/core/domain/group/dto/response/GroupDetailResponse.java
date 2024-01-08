package site.timecapsulearchive.core.domain.group.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Schema(description = "그룹 생성 포맷")
@Validated
public record GroupDetailResponse(
    @JsonProperty("name")
    @Schema(description = "그룹 이름")
    String name,

    @JsonProperty("createdDate")
    @Schema(description = "그룹 생성일")
    ZonedDateTime createdDate,

    @JsonProperty("profileUrl")
    @Schema(description = "그룹 프로필 url")
    String profileUrl,

    @JsonProperty("description")
    @Schema(description = "그룹 설명")
    String description,

    @JsonProperty("members")
    @Schema(description = "그룹원 리스트")
    @Valid
    List<GroupMemberDetailResponse> members
) {

}