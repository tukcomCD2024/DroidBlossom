package site.timecapsulearchive.core.domain.capsule.group_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import java.util.List;
import site.timecapsulearchive.core.domain.group.data.response.GroupMemberSummaryResponse;

@Schema(description = "그룹 캡슐 상세 정보")
public record GroupCapsuleDetailResponse(

    @Schema(description = "캡슐 스킨 url")
    String capsuleSkinUrl,

    @Schema(description = "그룹원 요약 정보")
    List<GroupMemberSummaryResponse> members,

    @Schema(description = "개봉일")
    ZonedDateTime dueDate,

    @Schema(description = "생성자 닉네임")
    String nickname,

    @Schema(description = "캡슐 생성일")
    ZonedDateTime createdDate,

    @Schema(description = "캡슐 생성 주소")
    String address,

    @Schema(description = "제목")
    String title,

    @Schema(description = "내용")
    String content,

    @Schema(description = "미디어 url들")
    List<String> mediaUrls,

    @Schema(description = "개봉 여부")
    Boolean isOpened
) {

}