package site.timecapsulearchive.core.domain.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import site.timecapsulearchive.core.domain.auth.dto.response.TemporaryTokenResponse;
import site.timecapsulearchive.core.domain.auth.dto.response.TokenResponse;

@Validated
public interface AuthApi {
    @Operation(
        summary = "카카오 로그인 페이지",
        description = "oauth2 kakao 인증 페이지 url을 가져온다. 로그인에 성공하면 임시 인증 토큰 토큰을 발급받는다.",
        tags = {"auth"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TemporaryTokenResponse.class)
            )
        )
    })
    @GetMapping(
        value = "/auth/login/kakao",
        produces = {"application/json"}
    )
    ResponseEntity<TemporaryTokenResponse> getOAuth2KakaoPage();


    @Operation(
        summary = "구글 로그인 페이지",
        description = "oauth2 google 인증 페이지 url을 가져온다. 로그인에 성공하면 임시 인증 토큰 토큰을 발급받는다.",
        tags = {"auth"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TemporaryTokenResponse.class)
            )
        )
    })
    @GetMapping(
        value = "/auth/login/google",
        produces = {"application/json"}
    )
    ResponseEntity<TemporaryTokenResponse> getOAuth2GooglePage();


    @Operation(
        summary = "액세스 토큰 재발급",
        description = "사용자의 액세스 토큰 재발급한다.",
        tags = {"auth"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TokenResponse.class)
            )
        )
    })
    @PostMapping(
        value = "/auth/token/re-issue",
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    ResponseEntity<TokenResponse> reIssueAccessToken();


    @Operation(
        summary = "인증 문자 전송",
        description = "사용자 인증을 위해 인증 문자를 전송한다. 5분 동안 유효하다.",
        security = {@SecurityRequirement(name = "temporary_user_token")},
        tags = {"auth"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202",
            description = "처리 시작"
        )
    })
    @PostMapping(
        value = "/auth/verification/send-message",
        consumes = {"application/json"}
    )
    ResponseEntity<Void> sendVerificationMessage();


    @Operation(
        summary = "문자 인증",
        description = "전송 받은 문자 번호가 유효한지 인증한다.",
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
        value = "/auth/verification/valid-message/",
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    ResponseEntity<Void> validVerificationMessage();
}

