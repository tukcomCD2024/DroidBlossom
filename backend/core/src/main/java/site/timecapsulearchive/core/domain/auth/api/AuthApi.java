package site.timecapsulearchive.core.domain.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import site.timecapsulearchive.core.domain.auth.dto.request.SignUpRequest;
import site.timecapsulearchive.core.domain.auth.dto.request.TokenReIssueRequest;
import site.timecapsulearchive.core.domain.auth.dto.request.VerificationMessageSendRequest;
import site.timecapsulearchive.core.domain.auth.dto.request.VerificationNumberValidRequest;
import site.timecapsulearchive.core.domain.auth.dto.response.OAuthUrlResponse;
import site.timecapsulearchive.core.domain.auth.dto.response.TemporaryTokenResponse;
import site.timecapsulearchive.core.domain.auth.dto.response.TokenResponse;
import site.timecapsulearchive.core.domain.auth.dto.response.VerificationMessageSendResponse;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.error.ErrorResponse;

public interface AuthApi {

    @Operation(
        summary = "카카오 로그인 페이지",
        description = "oauth2 kakao 인증 페이지 url을 가져온다.",
        tags = {"auth"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    @GetMapping(
        value = "/login/kakao",
        produces = {"application/json"}
    )
    ResponseEntity<OAuthUrlResponse> getOAuth2KakaoUrl();


    @Operation(
        summary = "구글 로그인 페이지",
        description = "oauth2 google 인증 페이지 url을 가져온다.",
        tags = {"auth"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    @GetMapping(
        value = "/login/google",
        produces = {"application/json"}
    )
    ResponseEntity<OAuthUrlResponse> getOAuth2GoogleUrl();

    @Operation(
        summary = "카카오 인증 성공시 임시 인증 토큰 발급",
        description = "oauth2 kakao 인증 성공시 임시 인증 토큰을 발급한다. (oauth2 로그인 성공시 리다이렉트 엔드포인트로 문서화 목적) ",
        tags = {"auth"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    @GetMapping(
        value = "/login/oauth2/code/kakao",
        produces = {"application/json"}
    )
    ResponseEntity<TemporaryTokenResponse> getTemporaryTokenResponseByKakao();


    @Operation(
        summary = "구글 인증 성공시 임시 인증 토큰 발급",
        description = "oauth2 google 인증 성공시 임시 인증 토큰을 발급한다. (oauth2 로그인 성공시 리다이렉트 엔드포인트로 문서화 목적) ",
        tags = {"auth"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    @GetMapping(
        value = "/login/oauth2/code/google",
        produces = {"application/json"}
    )
    ResponseEntity<TemporaryTokenResponse> getTemporaryTokenResponseByGoogle();

    @Operation(
        summary = "다른 소셜 프로바이더의 앱으로 인증한 클라이언트 저장 후 토큰 반환",
        description = "다른 소셜 프로바이더의 앱으로 인증한 클라이언트의 정보를 저장하고 임시 인증 토큰(1시간)을 반환한다.",
        tags = {"auth"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    @PostMapping(
        value = "/sign-up",
        produces = {"application/json"}
    )
    ResponseEntity<ApiSpec<TemporaryTokenResponse>> signUpWithSocialProvider(SignUpRequest request);

    @Operation(
        summary = "액세스 토큰 재발급",
        description = "사용자의 액세스 토큰 재발급한다.",
        tags = {"auth"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        ),
        @ApiResponse(
            responseCode = "400",
            description = """
            요청이 잘못되어 발생하는 오류이다.
            <ul>
            <li>올바르지 않은 요청인 경우 예외가 발생한다.</li>
            <li>유효하지 않은 리프레시 토큰인 경우 오류가 발생한다.</li>
            <li>이미 재발급에 사용된 리프레시 토큰인 경우 오류가 발생한다.</li>
            </ul>
            """,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping(
        value = "/token/re-issue",
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    ResponseEntity<ApiSpec<TokenResponse>> reIssueAccessToken(TokenReIssueRequest request);


    @Operation(
        summary = "인증 문자 전송",
        description = "사용자 인증을 위해 인증 문자를 전송한다. 5분 동안 유효하다.",
        security = {@SecurityRequirement(name = "temporary_user_token")},
        tags = {"auth"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202",
            description = "문자 전송 처리 시작"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증 수단이 올바르지 않으면 오류가 발생한다."
        ),
        @ApiResponse(
            responseCode = "429",
            description = "24시간 이내 5회 초과 요청시 API 요청 제한 오류가 발생한다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = """
                외부 문자 발송 API 오류 시 발생한다. 오류 응답은 링크 참조 \n
                <a href="https://smartsms.aligo.in/admin/api/spec.html">알리고 api</href>
                """,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping(
        value = "/verification/send-message",
        consumes = {"application/json"},
        produces = {"application/json"}
    )
    ResponseEntity<ApiSpec<VerificationMessageSendResponse>> sendVerificationMessage(
        Long memberId,
        VerificationMessageSendRequest request
    );


    @Operation(
        summary = "문자 인증",
        description = "전송 받은 문자 번호가 유효한지 인증 후 토큰을 발급한다.",
        security = {@SecurityRequirement(name = "temporary_user_token")},
        tags = {"auth"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    @PostMapping(
        value = "/verification/valid-message",
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    ResponseEntity<ApiSpec<TokenResponse>> validVerificationMessage(
        Long memberId,
        VerificationNumberValidRequest request
    );
}

