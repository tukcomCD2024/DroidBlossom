package site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.dto;

import java.util.function.UnaryOperator;
import site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.response.TreasureCapsuleOpenResponse;

public record TreasureCapsuleOpenDto(
    TreasureOpenStatus treasureOpenStatus,
    String treasureImageUrl
) {

    public TreasureCapsuleOpenResponse toResponse(
        final UnaryOperator<String> s3PreSignedUrlForGet) {
        String imageUrl = s3PreSignedUrlForGet.apply(treasureImageUrl);

        return new TreasureCapsuleOpenResponse(treasureOpenStatus, imageUrl);
    }
}
