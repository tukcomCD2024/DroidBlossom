package site.timecapsulearchive.core.domain.notification.api;

import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.notification.data.response.MemberNotificationSliceResponse;
import site.timecapsulearchive.core.domain.notification.data.dto.MemberNotificationDto;
import site.timecapsulearchive.core.domain.notification.service.NotificationService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationApiController implements NotificationApi {

    private final NotificationService notificationService;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;

    @Override
    @GetMapping(value = "/notifications")
    public ResponseEntity<ApiSpec<MemberNotificationSliceResponse>> getMemberNotifications(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(defaultValue = "20", value = "size") final int size,
        @RequestParam(value = "created_at") final ZonedDateTime createdAt
    ) {
        Slice<MemberNotificationDto> notificationSlice = notificationService.findNotificationSliceByMemberId(
            memberId, size, createdAt);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                MemberNotificationSliceResponse.createOf(
                    notificationSlice.getContent(),
                    notificationSlice.hasNext(),
                    s3PreSignedUrlManager::getS3PreSignedUrlForGet
                )
            )
        );
    }

}
