package site.timecapsulearchive.core.domain.member.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.member.data.reqeust.CheckStatusRequest;
import site.timecapsulearchive.core.domain.member.data.reqeust.UpdateFCMTokenRequest;
import site.timecapsulearchive.core.domain.member.data.reqeust.UpdateNotificationEnabledRequest;
import site.timecapsulearchive.core.domain.member.data.response.CheckEmailDuplicationResponse;
import site.timecapsulearchive.core.domain.member.data.response.MemberDetailResponse;
import site.timecapsulearchive.core.domain.member.data.response.MemberNotificationSliceResponse;
import site.timecapsulearchive.core.domain.member.data.response.MemberStatusResponse;
import site.timecapsulearchive.core.domain.member.service.MemberService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me")
public class MemberApiController implements MemberApi {

    private final MemberService memberService;

    @GetMapping(produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<MemberDetailResponse>> getMemberDetail(
        @AuthenticationPrincipal final Long memberId
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                memberService.findMemberDetailById(memberId)
            )
        );
    }

    @PostMapping(
        value = "/status",
        consumes = {"application/json"},
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<MemberStatusResponse>> checkMemberStatus(
        @Valid @RequestBody final CheckStatusRequest request
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                memberService.checkStatus(
                    request.authId(),
                    request.socialType()
                )
            )
        );
    }

    @PostMapping(
        value = "/status/email",
        consumes = {"application/json"},
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<MemberStatusResponse>> checkMemberStatusWithEmail(
        @RequestParam(value = "email") @Email String email) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                memberService.checkStatusWithEmail(email)
            )
        );
    }

    @Override
    @PatchMapping(value = "/fcm_token")
    public ResponseEntity<ApiSpec<String>> updateMemberFCMToken(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody final UpdateFCMTokenRequest request
    ) {
        memberService.updateMemberFCMToken(memberId, request.fcmToken());

        return ResponseEntity.ok(
            ApiSpec.empty(SuccessCode.SUCCESS)
        );
    }

    @Override
    @PatchMapping(value = "/notification_enabled")
    public ResponseEntity<ApiSpec<String>> updateMemberNotificationEnabled(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody final UpdateNotificationEnabledRequest request
    ) {
        memberService.updateMemberNotificationEnabled(memberId, request.notificationEnabled());

        return ResponseEntity.ok(ApiSpec.empty(SuccessCode.SUCCESS));
    }

    @Override
    @GetMapping(value = "/notifications")
    public ResponseEntity<ApiSpec<MemberNotificationSliceResponse>> getMemberNotifications(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(defaultValue = "20", value = "size") final int size,
        @RequestParam(defaultValue = "0", value = "createdAt") final ZonedDateTime createdAt
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                memberService.findNotificationSliceByMemberId(memberId, size, createdAt)
            )
        );
    }

    @GetMapping("/check-duplication/email")
    @Override
    public ResponseEntity<ApiSpec<CheckEmailDuplicationResponse>> checkEmailDuplication(
        @RequestParam(value = "email") @Email final String email
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                memberService.checkEmailDuplication(email)
            )
        );
    }
}
