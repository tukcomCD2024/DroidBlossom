package site.timecapsulearchive.core.domain.capsule.api.secret_c;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.reqeust.SecretCapsuleCreateRequest;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.reqeust.SecretCapsuleUpdateRequest;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.response.SecretCapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.response.SecretCapsuleSummaryResponse;
import site.timecapsulearchive.core.global.common.response.ApiSpec;

public interface SecretCapsuleApi {

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
        )
    })
    @PostMapping(
        value = "/capsules",
        consumes = {"application/json"}
    )
    ResponseEntity<ApiSpec<String>> createSecretCapsule(
        Long memberId,
        SecretCapsuleCreateRequest request
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
        )
    })
    @GetMapping(
        value = "/capsules/{capsule_id}/detail",
        produces = {"application/json"}
    )
    ResponseEntity<ApiSpec<SecretCapsuleDetailResponse>> getSecretCapsuleDetail(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "비밀 캡슐 아이디", required = true)
        Long capsuleId
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
        )
    })
    @GetMapping(
        value = "/capsules/{capsule_id}/summary",
        produces = {"application/json"}
    )
    ResponseEntity<ApiSpec<SecretCapsuleSummaryResponse>> getSecretCapsuleSummary(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "비밀 캡슐 아이디", required = true)
        Long capsuleId
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
    @PatchMapping(
        value = "/capsules/{capsule_id}",
        consumes = {"multipart/form-data"}
    )
    ResponseEntity<SecretCapsuleSummaryResponse> updateSecretCapsule(
        @Parameter(in = ParameterIn.PATH, description = "비밀 캡슐 아이디", required = true, schema = @Schema())
        @PathVariable("capsule_id") Long capsuleId,

        @ModelAttribute SecretCapsuleUpdateRequest request
    );
}