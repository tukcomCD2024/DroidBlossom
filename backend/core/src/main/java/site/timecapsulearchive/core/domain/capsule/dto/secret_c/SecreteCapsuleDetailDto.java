package site.timecapsulearchive.core.domain.capsule.dto.secret_c;

import java.time.ZonedDateTime;
import java.util.List;

public record SecreteCapsuleDetailDto(

    String capsuleSkinUrl,
    ZonedDateTime dueDate,
    String nickname,
    ZonedDateTime createdAt,
    String address,
    String title,
    String content,
    List<String> images,
    List<String> videos,
    Boolean isOpened
) {

}
