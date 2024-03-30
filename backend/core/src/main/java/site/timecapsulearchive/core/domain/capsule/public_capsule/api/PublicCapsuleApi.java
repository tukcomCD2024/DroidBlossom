package site.timecapsulearchive.core.domain.capsule.public_capsule.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.reqeust.PublicCapsuleCreateRequest;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.reqeust.PublicCapsuleUpdateRequest;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.response.PublicCapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.response.PublicCapsulePageResponse;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.response.PublicCapsuleSummaryResponse;

public interface PublicCapsuleApi {

    @Operation(
        summary = "공개 캡슐 생성",
        description = "사용자의 친구들만 볼 수 있는 공개 캡슐을 생성한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"public capsule"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202",
            description = "처리 시작"
        )
    })
    @PostMapping(
        value = "/capsules",
        consumes = {"multipart/form-data"}
    )
    ResponseEntity<PublicCapsuleSummaryResponse> createPublicCapsule(
        @ModelAttribute PublicCapsuleCreateRequest request
    );

    @Operation(
        summary = "공개 캡슐 상세 조회",
        description = "사용자의 친구들만 볼 수 있는 공개 캡슐 내용을 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"public capsule"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    @GetMapping(
        value = "/capsules/{capsule_id}",
        produces = {"application/json"}
    )
    ResponseEntity<PublicCapsuleDetailResponse> findPublicCapsuleById(
        @Parameter(in = ParameterIn.PATH, description = "조회할 캡슐 아이디", required = true, schema = @Schema())
        @PathVariable("capsule_id") Long capsuleId
    );

    @Operation(
        summary = "공개 캡슐 목록 조회",
        description = "친구만 볼 수 있는 공개 캡슐 목록을 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"public capsule"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    @GetMapping(
        value = "/capsules",
        produces = {"application/json"}
    )
    ResponseEntity<PublicCapsulePageResponse> getPublicCapsules(
        @Parameter(in = ParameterIn.QUERY, description = "페이지 크기", required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "size") Long size,

        @Parameter(in = ParameterIn.QUERY, description = "마지막 캡슐 아이디", required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "capsule_id") Long capsuleId
    );

    @Operation(
        summary = "공개 캡슐 24시간 이내 수정",
        description = "사용자가 생성한 공개 캡슐의 생성 시간이 24시간 이내라면 수정한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"public capsule"}
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
    ResponseEntity<PublicCapsuleSummaryResponse> updatePublicCapsuleById(
        @Parameter(in = ParameterIn.PATH, description = "공개 캡슐 아이디", required = true, schema = @Schema(implementation = Long.class))
        @PathVariable("capsule_id") Long capsuleId,

        @ModelAttribute PublicCapsuleUpdateRequest request
    );
}

