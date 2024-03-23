package site.timecapsulearchive.core.domain.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.ZonedDateTime;
import org.springframework.http.ResponseEntity;
import site.timecapsulearchive.core.domain.member.data.reqeust.CheckStatusRequest;
import site.timecapsulearchive.core.domain.member.data.reqeust.UpdateFCMTokenRequest;
import site.timecapsulearchive.core.domain.member.data.reqeust.UpdateNotificationEnabledRequest;
import site.timecapsulearchive.core.domain.member.data.response.CheckEmailDuplicationResponse;
import site.timecapsulearchive.core.domain.member.data.response.MemberDetailResponse;
import site.timecapsulearchive.core.domain.member.data.response.MemberNotificationSliceResponse;
import site.timecapsulearchive.core.domain.member.data.response.MemberStatusResponse;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.error.ErrorResponse;

public interface MemberApi {

    @Operation(
        summary = "회원 상세 정보 조회",
        description = "회원의 상세 정보를 조회한다.",
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
    ResponseEntity<ApiSpec<MemberDetailResponse>> getMemberDetail(Long memberId);

    @Operation(
        summary = "다른 OAuth2 프로바이더의 아이디로 앱 내의 유저의 인증 상태를 반환",
        description = "Google, Kakao 프로바이더가 제공하는 유저 아이디로 앱 내의 인증 상태를 반환한다.",
        tags = {"member"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    ResponseEntity<ApiSpec<MemberStatusResponse>> checkMemberStatus(CheckStatusRequest request);

    @Operation(
        summary = "회원 FCM 토큰 수정",
        description = "회원의 FCM 토큰을 수정한다.",
        security = {@SecurityRequirement(name = "member")},
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
    ResponseEntity<ApiSpec<String>> updateMemberFCMToken(
        Long memberId,
        UpdateFCMTokenRequest request
    );

    @Operation(
        summary = "회원 알림 수신 상태 수정",
        description = "회원의 알림 수신 상태를 수정한다.",
        security = {@SecurityRequirement(name = "member")},
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
    ResponseEntity<ApiSpec<String>> updateMemberNotificationEnabled(
        Long memberId,
        UpdateNotificationEnabledRequest updateNotificationEnabledRequest
    );

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

    @Operation(
        summary = "이메일 중복 조회",
        description = "이메일의 중복을 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"member"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        )
    })
    ResponseEntity<ApiSpec<CheckEmailDuplicationResponse>> checkEmailDuplication(String email);
}
