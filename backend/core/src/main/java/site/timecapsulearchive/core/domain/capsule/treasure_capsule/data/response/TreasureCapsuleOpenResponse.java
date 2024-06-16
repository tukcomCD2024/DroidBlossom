package site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.function.Function;
import site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.dto.TreasureCapsuleOpenDto;

@Schema(name = "보물 캡슐 개봉 응답")
public record TreasureCapsuleOpenResponse(
    @Schema(name = "보물 이미지 URL")
    String treasureImageUrl
) {

    public static TreasureCapsuleOpenResponse createOf(
        final TreasureCapsuleOpenDto dto,
        final Function<String, String> s3PreSignedUrlForGet
    ) {
        return dto.toResponse(s3PreSignedUrlForGet);
    }

}
