package site.timecapsulearchive.core.domain.member.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.member.data.reqeust.UpdateMemberPhoneSearchAvailableRequest;
import site.timecapsulearchive.core.domain.member.data.reqeust.UpdateMemberTagSearchAvailableRequest;
import site.timecapsulearchive.core.domain.member.data.dto.MemberDetailDto;
import site.timecapsulearchive.core.domain.member.data.reqeust.CheckStatusRequest;
import site.timecapsulearchive.core.domain.member.data.reqeust.UpdateFCMTokenRequest;
import site.timecapsulearchive.core.domain.member.data.reqeust.UpdateMemberDataRequest;
import site.timecapsulearchive.core.domain.member.data.reqeust.UpdateNotificationEnabledRequest;
import site.timecapsulearchive.core.domain.member.data.response.MemberDetailResponse;
import site.timecapsulearchive.core.domain.member.data.response.MemberNotificationStatusResponse;
import site.timecapsulearchive.core.domain.member.data.response.MemberStatusResponse;
import site.timecapsulearchive.core.domain.member.facade.MemberFacade;
import site.timecapsulearchive.core.domain.member.service.MemberService;
import site.timecapsulearchive.core.global.common.argument.AccessToken;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me")
public class MemberApiController implements MemberApi {

    private final MemberFacade memberFacade;
    private final MemberService memberService;

    @GetMapping(produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<MemberDetailResponse>> getMemberDetail(
        @AuthenticationPrincipal final Long memberId
    ) {
        final MemberDetailDto detailDto = memberService.findMemberDetailById(memberId);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                MemberDetailResponse.createOf(detailDto)
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

    @PatchMapping(value = "/fcm_token")
    @Override
    public ResponseEntity<ApiSpec<String>> updateMemberFCMToken(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody final UpdateFCMTokenRequest request
    ) {
        memberService.updateMemberFCMToken(memberId, request.fcmToken());

        return ResponseEntity.ok(
            ApiSpec.empty(SuccessCode.SUCCESS)
        );
    }

    @PatchMapping(value = "/notification_enabled")
    @Override
    public ResponseEntity<ApiSpec<String>> updateMemberNotificationEnabled(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody final UpdateNotificationEnabledRequest request
    ) {
        memberService.updateMemberNotificationEnabled(memberId, request.notificationEnabled());

        return ResponseEntity.ok(ApiSpec.empty(SuccessCode.SUCCESS));
    }

    @GetMapping(value = "/notification_enabled")
    @Override
    public ResponseEntity<ApiSpec<MemberNotificationStatusResponse>> checkMemberNotificationStatus(
        @AuthenticationPrincipal Long memberId) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                memberService.checkNotificationStatus(memberId)
            )
        );
    }

    @PatchMapping("/data")
    @Override
    public ResponseEntity<ApiSpec<String>> updateMemberData(
        @AuthenticationPrincipal Long memberId,
        @Valid @RequestBody UpdateMemberDataRequest request
    ) {
        memberService.updateMemberData(memberId, request.toDto());

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.SUCCESS
            )
        );
    }

    @PatchMapping("/tag-search-available")
    @Override
    public ResponseEntity<ApiSpec<String>> updateMemberTagSearchAvailable(
        @AuthenticationPrincipal Long memberId,
        @Valid @RequestBody UpdateMemberTagSearchAvailableRequest request
    ) {
        memberService.updateMemberTagSearchAvailable(memberId, request.tagSearchAvailable());

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.SUCCESS
            )
        );
    }

    @PatchMapping("/phone-search-available")
    @Override
    public ResponseEntity<ApiSpec<String>> updateMemberTagSearchAvailable(
        @AuthenticationPrincipal Long memberId,
        @Valid @RequestBody UpdateMemberPhoneSearchAvailableRequest request
    ) {
        memberService.updateMemberPhoneSearchAvailable(memberId, request.phoneSearchAvailable());

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.SUCCESS
            )
        );
    }

    @DeleteMapping
    @Override
    public ResponseEntity<ApiSpec<String>> deleteMember(
        @AuthenticationPrincipal final Long memberId,
        @AccessToken final String accessToken
    ) {
        memberFacade.deleteByMemberId(memberId, accessToken);

        return ResponseEntity.ok(ApiSpec.empty(SuccessCode.SUCCESS));
    }
}
