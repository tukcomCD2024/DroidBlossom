package site.timecapsulearchive.core.domain.capsuleskin.api;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.timecapsulearchive.core.domain.capsuleskin.dto.reqeust.CapsuleSkinCreateRequest;
import site.timecapsulearchive.core.domain.capsuleskin.dto.response.CapsuleSkinSearchPageResponse;
import site.timecapsulearchive.core.domain.capsuleskin.dto.response.CapsuleSkinSummaryResponse;
import site.timecapsulearchive.core.domain.capsuleskin.dto.response.CapsuleSkinsPageResponse;

@Validated
public interface CapsuleSkinApi {
    @Operation(
        summary = "캡슐 스킨 생성",
        description = "정해진 포맷으로 캡슐 스킨을 생성한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"capsule skin"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202",
            description = "처리 시작",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CapsuleSkinSummaryResponse.class)
            )
        )
    })
    @PostMapping(
        value = "/capsule-skins",
        consumes = {"multipart/form-data"}
    )
    ResponseEntity<CapsuleSkinSummaryResponse> createCapsuleSkin(
        @ModelAttribute CapsuleSkinCreateRequest request
    );

    @Operation(
        summary = "캡슐 스킨 삭제",
        description = "사용자가 소유한 캡슐 스킨을 삭제한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"capsule skin"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "처리 완료"
        )
    })
    @DeleteMapping(value = "/capsule-skins/{capsule_skin_id}")
    ResponseEntity<Void> deleteCapsuleSkin(
        @Parameter(in = ParameterIn.PATH, description = "캡슐 스킨 아이디", required = true, schema = @Schema())
        @PathVariable("capsule_skin_id") Long capsuleSkinId
    );

    @Operation(
        summary = "캡슐 스킨 이름 검색",
        description = "캡슐 스킨 이름으로 사용자 소유의 스킨을 검색한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"capsule skin"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CapsuleSkinSearchPageResponse.class)
            )
        )
    })
    @GetMapping(
        value = "/capsule-skins/search",
        produces = {"application/json"}
    )
    ResponseEntity<CapsuleSkinSearchPageResponse> findCapsuleByName(
        @Parameter(in = ParameterIn.QUERY, description = "캡슐 스킨 이름", required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "capsule_skin_name") Long capsuleSkinName,

        @Parameter(in = ParameterIn.QUERY, description = "페이지 크기", required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "size") Long size,

        @Parameter(in = ParameterIn.QUERY, description = "마지막 스킨 아이디", required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "capsule_skin_id") Long capsuleSkinId
    );

    @Operation(
        summary = "캡슐 스킨 목록 조회",
        description = "사용자가 소유한 캡슐 스킨 목록을 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"capsule skin"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CapsuleSkinsPageResponse.class)
            )
        )
    })
    @GetMapping(
        value = "/capsule-skins",
        produces = {"application/json"}
    )
    ResponseEntity<CapsuleSkinsPageResponse> findCapsuleSkins(
        @Parameter(in = ParameterIn.QUERY, description = "페이지 크기", required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "size") Long size,

        @Parameter(in = ParameterIn.QUERY, description = "마지막 스킨 아이디", required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "capsule_skin_id") Long capsuleSkinId
    );

    @Operation(
        summary = "캡슐 스킨 수정",
        description = "캡슐 스킨의 기본 정보를 수정한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"capsule skin"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        )
    })
    @PatchMapping(
        value = "/capsule-skins/{capsule_skin_id}",
        consumes = {"application/json"})

    ResponseEntity<Void> updateCapsuleSkin(
        @Parameter(in = ParameterIn.PATH, description = "캡슐 스킨 아이디", required = true, schema = @Schema())
        @PathVariable("capsule_skin_id") Long capsuleSkinId
    );
}
