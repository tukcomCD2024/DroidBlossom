package site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.dto;

import java.util.function.Function;
import site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.response.TreasureCapsuleOpenResponse;

public record TreasureCapsuleOpenDto(
    String treasureImageUrl
) {

    public TreasureCapsuleOpenResponse toResponse(final Function<String, String> s3PreSignedUrlForGet) {
        String imageUrl = s3PreSignedUrlForGet.apply(treasureImageUrl);
        return new TreasureCapsuleOpenResponse(imageUrl);
    }
}
