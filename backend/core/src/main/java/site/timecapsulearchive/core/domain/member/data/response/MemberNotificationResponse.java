package site.timecapsulearchive.core.domain.member.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import lombok.Builder;
import site.timecapsulearchive.core.domain.member.entity.CategoryName;
import site.timecapsulearchive.core.domain.member.entity.NotificationStatus;

@Schema(description = "알림 정보")
@Builder
public record MemberNotificationResponse(

    @Schema(description = "알림 제목")
    String title,

    @Schema(description = "알림 내용")
    String text,

    @Schema(description = "알림 생성일")
    ZonedDateTime createdAt,

    @Schema(description = "알림 이미지 링크")
    String imageUrl,

    @Schema(description = "알림 카테고리 이름")
    CategoryName categoryName,

    @Schema(description = "알림 상태")
    NotificationStatus status
) {

}
