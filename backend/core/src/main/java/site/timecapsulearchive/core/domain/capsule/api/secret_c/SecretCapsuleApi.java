package site.timecapsulearchive.core.domain.capsule.api.secret_c;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import site.timecapsulearchive.core.domain.capsule.data.secret_c.reqeust.SecretCapsuleCreateRequest;
import site.timecapsulearchive.core.domain.capsule.data.secret_c.reqeust.SecretCapsuleUpdateRequest;
import site.timecapsulearchive.core.domain.capsule.data.secret_c.response.MySecretCapsulePageResponse;
import site.timecapsulearchive.core.domain.capsule.data.secret_c.response.SecretCapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.data.secret_c.response.SecretCapsuleSummaryResponse;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.error.ErrorResponse;

public interface SecretCapsuleApi {

    @Operation(
        summary = "내 비밀 캡슐 목록 조회",
        description = "사용자가 생성한 비밀 캡슐 목록을 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"secret capsule"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        )
    })
    ResponseEntity<ApiSpec<MySecretCapsulePageResponse>> getMySecretCapsules(
        Long memberId,

        @Parameter(in = ParameterIn.QUERY, description = "페이지 크기", required = true)
        @NotNull @Valid @RequestParam(value = "size") int size,

        @Parameter(in = ParameterIn.QUERY, description = "마지막 캡슐 생성 시간", required = true)
        @NotNull @Valid @RequestParam(value = "createdAt") ZonedDateTime createdAt
    );

    @Operation(
        summary = "비밀 캡슐 상세 조회",
        description = "사용자만 볼 수 있는 비밀 캡슐의 내용을 상세 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"secret capsule"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "해당 캡슐을 찾을 수 없을 경우 발생하는 예외",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<ApiSpec<SecretCapsuleDetailResponse>> getSecretCapsuleDetail(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "비밀 캡슐 아이디", required = true)
        @PathVariable("capsule_id") Long capsuleId
    );

    @Operation(
        summary = "비밀 캡슐 요약 조회",
        description = "사용자만 볼 수 있는 비밀 캡슐의 내용을 요약 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"secret capsule"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "해당 캡슐을 찾을 수 없을 경우 발생하는 예외",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<ApiSpec<SecretCapsuleSummaryResponse>> getSecretCapsuleSummary(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "비밀 캡슐 아이디", required = true)
        @PathVariable("capsule_id") Long capsuleId
    );

    @Operation(
        summary = "비밀 캡슐 생성",
        description = "사용자만 볼 수 있는 비밀 캡슐을 생성한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"secret capsule"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202",
            description = "처리 시작"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "좌표변환을 할 수 없을 때 발생하는 예외, 입력좌표 확인 요망",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "캡슐 스킨을 찾을 수 없을 경우 발생하는 예외",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<ApiSpec<String>> createSecretCapsule(
        Long memberId,
        SecretCapsuleCreateRequest request
    );

    @Operation(
        summary = "비밀 캡슐 수정",
        description = "사용자가 생성한 비밀 캡슐의 생성 시간이 24시간 이내라면 수정한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"secret capsule"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        )
    })
    ResponseEntity<SecretCapsuleSummaryResponse> updateSecretCapsule(
        @Parameter(in = ParameterIn.PATH, description = "비밀 캡슐 아이디", required = true)
        @PathVariable("capsule_id") Long capsuleId,

        @ModelAttribute SecretCapsuleUpdateRequest request
    );
}