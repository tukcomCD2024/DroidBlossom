package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import java.util.function.Function;
import lombok.Builder;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.global.common.response.ResponseMappingConstant;

@Schema(description = "캡슐 요약 정보")
@Builder
public record CapsuleSummaryResponse(

    @Schema(description = "생성자 닉네임")
    String nickname,

    @Schema(description = "생성자 프로필 url")
    String profileUrl,

    @Schema(description = "캡슐 스킨 url")
    String skinUrl,

    @Schema(description = "제목")
    String title,

    @Schema(description = "개봉일")
    ZonedDateTime dueDate,

    @Schema(description = "캡슐 생성 주소")
    String address,

    @Schema(description = "캡슐 생성 도로 이름")
    String roadName,

    @Schema(description = "개봉 여부")
    Boolean isOpened,

    @Schema(description = "캡슐 생성 일")
    ZonedDateTime createdAt
) {

    public CapsuleSummaryResponse {
        if (dueDate != null) {
            dueDate = dueDate.withZoneSameInstant(ResponseMappingConstant.ZONE_ID);
        }

        if (createdAt != null) {
            createdAt = createdAt.withZoneSameInstant(ResponseMappingConstant.ZONE_ID);
        }
    }

    public static CapsuleSummaryResponse createOf(
        final CapsuleSummaryDto summaryDto,
        final Function<String, String> preSignUrlFunction
    ) {
        return new CapsuleSummaryResponse(
            summaryDto.nickname(),
            summaryDto.profileUrl(),
            preSignUrlFunction.apply(summaryDto.skinUrl()),
            summaryDto.title(),
            summaryDto.dueDate(),
            summaryDto.address(),
            summaryDto.roadName(),
            summaryDto.isOpened(),
            summaryDto.createdAt()
        );
    }
}
