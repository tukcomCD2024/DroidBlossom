package site.timecapsulearchive.core.domain.announcement.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import site.timecapsulearchive.core.domain.announcement.data.dto.AnnouncementDto;

@Schema(description = "공지사항 목록 응답")
public record AnnouncementsResponse(
    @Schema(description = "공지사항 목록")
    List<AnnouncementResponse> announcements
) {

    public static AnnouncementsResponse createOf(List<AnnouncementDto> announcementDtos) {
        return new AnnouncementsResponse(
            announcementDtos.stream()
                .map(AnnouncementDto::toResponse)
                .toList()
        );
    }
}
