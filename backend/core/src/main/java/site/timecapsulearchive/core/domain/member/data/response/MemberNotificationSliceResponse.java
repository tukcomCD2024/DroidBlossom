package site.timecapsulearchive.core.domain.member.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "나의 알림 목록 응답")
public record MemberNotificationSliceResponse(

    @Schema(description = "나의 알림 목록")
    List<MemberNotificationResponse> responseList,

    @Schema(description = "다음 알림 목록 여부")
    boolean hasNext
) {

}
