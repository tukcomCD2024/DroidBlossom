package site.timecapsulearchive.core.domain.capsule.group_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.UnaryOperator;
import lombok.Builder;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.CombinedGroupCapsuleSummaryDto;
import site.timecapsulearchive.core.global.common.response.ResponseMappingConstant;

@Schema(description = "캡슐 요약 정보")
@Builder
public record GroupCapsuleSummaryResponse(

    @Schema(description = "그룹 아이디")
    Long groupId,

    @Schema(description = "그룹원 요약 정보")
    List<GroupCapsuleMemberResponse> groupMembers,

    @Schema(description = "그룹 이름")
    String groupName,

    @Schema(description = "그룹 프로필 url")
    String groupProfileUrl,

    @Schema(description = "생성자 닉네임")
    String creatorNickname,

    @Schema(description = "생성자 프로필 url")
    String creatorProfileUrl,

    @Schema(description = "캡슐 스킨 url")
    String skinUrl,

    @Schema(description = "제목")
    String title,

    @Schema(description = "개봉일")
    ZonedDateTime dueDate,

    @Schema(description = "캡슐 위도 좌표")
    Double latitude,

    @Schema(description = "캡슐 경도 좌표")
    Double longitude,

    @Schema(description = "캡슐 생성 주소")
    String address,

    @Schema(description = "캡슐 생성 도로 이름")
    String roadName,

    @Schema(description = "캡슐 개봉 여부")
    Boolean isCapsuleOpened,

    @Schema(description = "현재 사용자 캡슐 개봉 여부")
    Boolean isRequestMemberCapsuleOpened,

    @Schema(description = "캡슐 생성 일")
    ZonedDateTime createdAt
) {

    public GroupCapsuleSummaryResponse {
        if (dueDate != null) {
            dueDate = dueDate.withZoneSameInstant(ResponseMappingConstant.ZONE_ID);
        }

        createdAt = createdAt.withZoneSameInstant(ResponseMappingConstant.ZONE_ID);
    }

    public static GroupCapsuleSummaryResponse createOf(
        final CombinedGroupCapsuleSummaryDto groupCapsuleSummary,
        final UnaryOperator<String> preSignUrlFunction,
        final UnaryOperator<Point> changePointFunction
    ) {
        return groupCapsuleSummary.toResponse(preSignUrlFunction, changePointFunction);
    }
}
