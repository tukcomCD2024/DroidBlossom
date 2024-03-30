package site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;
import site.timecapsulearchive.core.infra.map.data.dto.AddressData;

@Builder
public record CapsuleCreateRequestDto(
    Long capsuleSkinId,
    String title,
    String content,
    Double longitude,
    Double latitude,
    AddressData addressData,
    ZonedDateTime dueDate,
    List<String> imageNames,
    List<String> videoNames,
    String directory
) {

}
