package site.timecapsulearchive.core.domain.announcement.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import site.timecapsulearchive.core.global.common.response.ResponseMappingConstant;

@Schema(description = "공지사항 응답")
public record AnnouncementResponse(
    @Schema(description = "제목")
    String title,

    @Schema(description = "내용")
    String content,

    @Schema(description = "공지사항 버전")
    String version,

    @Schema(description = "공지사항 생성일")
    ZonedDateTime createdAt
) {

    public AnnouncementResponse {
        if (createdAt != null) {
            createdAt = createdAt.withZoneSameInstant(ResponseMappingConstant.ZONE_ID);
        }
    }
}
