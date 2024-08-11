package site.timecapsulearchive.core.domain.announcement.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.announcement.data.dto.AnnouncementDto;
import site.timecapsulearchive.core.domain.announcement.data.response.AnnouncementsResponse;
import site.timecapsulearchive.core.domain.announcement.service.AnnouncementService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequestMapping("/announcement")
@RequiredArgsConstructor
public class AnnouncementApiController implements AnnouncementApi {

    private final AnnouncementService announcementService;

    @GetMapping
    @Override
    public ResponseEntity<ApiSpec<AnnouncementsResponse>> getAnnouncements() {
        List<AnnouncementDto> announcements = announcementService.findAll();

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                AnnouncementsResponse.createOf(announcements)
            )
        );
    }
}
