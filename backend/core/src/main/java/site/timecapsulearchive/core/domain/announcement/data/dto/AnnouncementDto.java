package site.timecapsulearchive.core.domain.announcement.data.dto;

import java.time.ZonedDateTime;
import site.timecapsulearchive.core.domain.announcement.data.response.AnnouncementResponse;

public record AnnouncementDto(
    String title,
    String content,
    String version,
    ZonedDateTime createdAt
) {

    public AnnouncementResponse toResponse() {
        return new AnnouncementResponse(title, content, version, createdAt);
    }
}
