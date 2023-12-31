package site.timecapsulearchive.core.domain.secretcapsule.api;

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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.timecapsulearchive.core.domain.secretcapsule.dto.reqeust.SecretCapsuleUpdateRequest;
import site.timecapsulearchive.core.domain.secretcapsule.dto.response.SecretCapsuleDetailResponse;
import site.timecapsulearchive.core.domain.secretcapsule.dto.response.SecretCapsulePageResponse;
import site.timecapsulearchive.core.domain.secretcapsule.dto.response.SecretCapsuleSummaryResponse;

@Validated
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
            description = "처리 시작",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SecretCapsuleSummaryResponse.class)
            )
        )
    })
    @PostMapping(
        value = "/secret/capsules",
        consumes = {"multipart/form-data"}
    )
    ResponseEntity<SecretCapsuleSummaryResponse> createSecretCapsule(
        @ModelAttribute SecretCapsuleUpdateRequest request
    );

    @Operation(
        summary = "비밀 캡슐 상세 조회",
        description = "사용자만 볼 수 있는 비밀 캡슐의 내용을 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"secret capsule"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SecretCapsuleDetailResponse.class)
            )
        )
    })
    @GetMapping(
        value = "/secret/capsules/{capsule_id}",
        produces = {"application/json"}
    )
    ResponseEntity<SecretCapsuleDetailResponse> findSecretCapsuleById(
        @Parameter(in = ParameterIn.PATH, description = "비밀 캡슐 아이디", required = true, schema = @Schema())
        @PathVariable("capsule_id") Long capsuleId
    );

    @Operation(
        summary = "내 비밀 캡슐 목록 조회",
        description = "사용자만 볼 수 있는 비밀 캡슐 목록 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"secret capsule"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SecretCapsulePageResponse.class)
            )
        )
    })
    @GetMapping(
        value = "/secret/capsules",
        produces = {"application/json"}
    )
    ResponseEntity<SecretCapsulePageResponse> getSecretCapsules(
        @Parameter(in = ParameterIn.QUERY, description = "페이지 크기", required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "size") Long size,

        @Parameter(in = ParameterIn.QUERY, description = "마지막 캡슐 아이디", required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "capsule_id") Long capsuleId
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
            description = "처리 완료",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SecretCapsuleSummaryResponse.class)
            )
        )
    })
    @PatchMapping(
        value = "/secret/capsules/{capsule_id}",
        consumes = {"multipart/form-data"}
    )
    ResponseEntity<SecretCapsuleSummaryResponse> updateSecretCapsule(
        @Parameter(in = ParameterIn.PATH, description = "비밀 캡슐 아이디", required = true, schema = @Schema())
        @PathVariable("capsule_id") Long capsuleId,

        @ModelAttribute SecretCapsuleUpdateRequest request
    );
}