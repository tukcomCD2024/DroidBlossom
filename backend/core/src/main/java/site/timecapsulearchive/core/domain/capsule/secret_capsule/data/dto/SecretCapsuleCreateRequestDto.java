package site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.AddressData;

@Builder
public record SecretCapsuleCreateRequestDto(
    Long capsuleSkinId,
    String title,
    String content,
    double longitude,
    double latitude,
    AddressData addressData,
    ZonedDateTime dueDate,
    List<String> imageNames,
    List<String> videoNames,
    String directory
) {

}
