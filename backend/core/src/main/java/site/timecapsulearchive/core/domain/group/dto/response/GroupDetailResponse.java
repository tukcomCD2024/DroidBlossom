package site.timecapsulearchive.core.domain.group.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import java.util.List;

@Schema(description = "그룹 생성 포맷")
public record GroupDetailResponse(

    @Schema(description = "그룹 이름")
    String name,

    @Schema(description = "그룹 생성일")
    ZonedDateTime createdDate,

    @Schema(description = "그룹 프로필 url")
    String profileUrl,

    @Schema(description = "그룹 설명")
    String description,

    @Schema(description = "그룹원 리스트")
    List<GroupMemberDetailResponse> members
) {

}