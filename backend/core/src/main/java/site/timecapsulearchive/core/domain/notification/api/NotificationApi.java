package site.timecapsulearchive.core.domain.notification.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.ZonedDateTime;
import org.springframework.http.ResponseEntity;
import site.timecapsulearchive.core.domain.notification.data.response.MemberNotificationSliceResponse;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.error.ErrorResponse;

public interface NotificationApi {

    @Operation(
        summary = "회원 알림 목록 조회",
        description = "회원의 알림 목록을 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"member"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "해당 멤버가 존재하지 않을 때 발생하는 예외",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
    })
    ResponseEntity<ApiSpec<MemberNotificationSliceResponse>> getMemberNotifications(
        Long memberId,
        int size,
        ZonedDateTime createdAt
    );
}
