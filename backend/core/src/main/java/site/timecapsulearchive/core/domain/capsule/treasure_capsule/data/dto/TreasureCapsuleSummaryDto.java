package site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.function.Function;
import lombok.Builder;
import site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.response.TreasureCapsuleSummaryResponse;

@Builder
public record TreasureCapsuleSummaryDto(
    String skinUrl,
    ZonedDateTime dueDate,
    String address,
    String roadName

) {

    public TreasureCapsuleSummaryResponse toResponse(final Function<String, String> s3PreSignedUrlForGet) {
        final String treasureSkinUrl = s3PreSignedUrlForGet.apply(skinUrl);

        return TreasureCapsuleSummaryResponse.builder()
            .skinUrl(treasureSkinUrl)
            .dueDate(dueDate)
            .address(address)
            .roadName(roadName)
            .build();
    }
}
