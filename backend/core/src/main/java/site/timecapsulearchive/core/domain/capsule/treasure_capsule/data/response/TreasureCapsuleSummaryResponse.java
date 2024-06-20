package site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import java.util.function.Function;
import lombok.Builder;
import site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.dto.TreasureCapsuleSummaryDto;

@Schema(description = "보물 캡슐 요약 정보")
@Builder
public record TreasureCapsuleSummaryResponse(

    @Schema(description = "보물 캡슐 스킨 url")
    String skinUrl,

    @Schema(description = "개봉일")
    ZonedDateTime dueDate,

    @Schema(description = "캡슐 생성 주소")
    String address,

    @Schema(description = "캡슐 생성 도로 이름")
    String roadName

) {

    public static TreasureCapsuleSummaryResponse createOf(
        final TreasureCapsuleSummaryDto dto,
        final Function<String, String> s3PreSignedUrlForGet
    ) {
        return dto.toResponse(s3PreSignedUrlForGet);
    }
}
