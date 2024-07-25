package site.timecapsulearchive.core.domain.announcement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import site.timecapsulearchive.core.domain.announcement.data.response.AnnouncementsResponse;
import site.timecapsulearchive.core.global.common.response.ApiSpec;

public interface AnnouncementApi {
    @Operation(
        summary = "공지사항 페이지",
        description = "모든 공지사항을 가져온다. 최신 공지가 리스트의 맨 처음에 위치한다.",
        tags = {"announcement"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    ResponseEntity<ApiSpec<AnnouncementsResponse>> getAnnouncements();
}
