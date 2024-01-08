package site.timecapsulearchive.core.domain.capsule.dto.group_c.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import site.timecapsulearchive.core.domain.group.dto.response.GroupMemberSummaryResponse;

@Schema(description = "그룹 캡슐 상세 정보")
@Validated
public record GroupCapsuleDetailResponse(
    @JsonProperty("capsuleSkinUrl")
    @Schema(description = "캡슐 스킨 url")
    String capsuleSkinUrl,

    @JsonProperty("members")
    @Schema(description = "그룹원 요약 정보")
    @Valid
    List<GroupMemberSummaryResponse> members,

    @JsonProperty("dueDate")
    @Schema(description = "개봉일")
    ZonedDateTime dueDate,

    @JsonProperty("nickname")
    @Schema(description = "생성자 닉네임")
    String nickname,

    @JsonProperty("createdDate")
    @Schema(description = "캡슐 생성일")
    ZonedDateTime createdDate,

    @JsonProperty("address")
    @Schema(description = "캡슐 생성 주소")
    String address,

    @JsonProperty("title")
    @Schema(description = "제목")
    String title,

    @JsonProperty("content")
    @Schema(description = "내용")
    String content,

    @JsonProperty("mediaUrls")
    @Schema(description = "미디어 url들")
    @Valid
    List<String> mediaUrls,

    @JsonProperty("isOpened")
    @Schema(description = "개봉 여부")
    Boolean isOpened
) {

}