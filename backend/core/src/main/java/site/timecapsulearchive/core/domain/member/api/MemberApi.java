package site.timecapsulearchive.core.domain.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import site.timecapsulearchive.core.domain.auth.data.request.VerificationMessageSendRequest;
import site.timecapsulearchive.core.domain.auth.data.request.VerificationNumberValidRequest;
import site.timecapsulearchive.core.domain.member.data.reqeust.CheckStatusRequest;
import site.timecapsulearchive.core.domain.member.data.reqeust.UpdateFCMTokenRequest;
import site.timecapsulearchive.core.domain.member.data.reqeust.UpdateMemberDataRequest;
import site.timecapsulearchive.core.domain.member.data.reqeust.UpdateMemberPhoneSearchAvailableRequest;
import site.timecapsulearchive.core.domain.member.data.reqeust.UpdateMemberTagSearchAvailableRequest;
import site.timecapsulearchive.core.domain.member.data.reqeust.UpdateNotificationEnabledRequest;
import site.timecapsulearchive.core.domain.member.data.response.MemberDetailResponse;
import site.timecapsulearchive.core.domain.member.data.response.MemberNotificationStatusResponse;
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
        summary = "회원 알림 수신 상태 확인",
        description = "회원의 알림 수신 상태를 확인한다.",
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
    ResponseEntity<ApiSpec<MemberNotificationStatusResponse>> checkMemberNotificationStatus(
        Long memberId
    );

    @Operation(
        summary = "사용자 정보 수정",
        description = "사용자 정보(닉네임, 태그)를 수정한다.",
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
    ResponseEntity<ApiSpec<String>> updateMemberData(
        Long memberId,
        UpdateMemberDataRequest request
    );

    @Operation(
        summary = "사용자 전화번호 변경을 위한 인증번호 전송",
        description = "사용자 전화번호 변경을 위해 인증번호를 전송한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"member"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202",
            description = "처리 시작"
        )
    })
    ResponseEntity<ApiSpec<String>> sendVerificationMessage(
        Long memberId,
        VerificationMessageSendRequest request
    );

    @Operation(
        summary = "사용자 전화번호 변경을 위한 인증번호 검증",
        description = "사용자 전화번호 변경을 위해 인증번호를 검증한다. 성공 시 전화번호가 업데이트된다.",
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
    ResponseEntity<ApiSpec<String>> validVerificationMessage(
        Long memberId,
        VerificationNumberValidRequest request
    );

    @Operation(
        summary = "사용자 태그 검색 허용 상태 수정",
        description = "사용자의 태그 검색 허용 상태를 수정한다.",
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
    ResponseEntity<ApiSpec<String>> updateMemberTagSearchAvailable(
        Long memberId,
        UpdateMemberTagSearchAvailableRequest request
    );

    @Operation(
        summary = "사용자 핸드폰 검색 허용 상태 수정",
        description = "사용자의 핸드폰 검색 허용 상태를 수정한다.",
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
    ResponseEntity<ApiSpec<String>> updateMemberPhoneSearchAvailable(
        Long memberId,
        UpdateMemberPhoneSearchAvailableRequest request
    );

    @Operation(
        summary = "회원 탈퇴",
        description = """
            가입한 사용자가 회원을 탈퇴한다.
            """,
        tags = {"member"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "사용자를 찾을 수 없는 경우 발생한다."
        )
    })
    ResponseEntity<ApiSpec<String>> deleteMember(
        Long memberId,

        @Parameter(hidden = true) String accessToken
    );

    @Operation(
        summary = "회원 신고",
        description = "부적절한 회원을 신고할 수 있다.",
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
            description = "해당 회원을 찾을 수 없을 경우 발생하는 예외",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<ApiSpec<String>> declarationMember(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "신고할 회원 아이디", required = true)
        Long targetId
    );

}
