package site.timecapsulearchive.core.domain.capsuleskin.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.ZonedDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import site.timecapsulearchive.core.domain.capsuleskin.dto.reqeust.CapsuleSkinCreateRequest;
import site.timecapsulearchive.core.domain.capsuleskin.dto.response.CapsuleSkinSearchPageResponse;
import site.timecapsulearchive.core.domain.capsuleskin.dto.response.CapsuleSkinSummaryResponse;
import site.timecapsulearchive.core.domain.capsuleskin.dto.response.CapsuleSkinsPageResponse;
import site.timecapsulearchive.core.global.common.response.ApiSpec;

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
            description = "처리 시작"
        )
    })
    @PostMapping(
        consumes = {"multipart/form-data"}
    )
    ResponseEntity<ApiSpec<CapsuleSkinSummaryResponse>> createCapsuleSkin(CapsuleSkinCreateRequest request);

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
    @DeleteMapping(value = "/{capsule_skin_id}")
    ResponseEntity<ApiSpec<String>> deleteCapsuleSkin(
        @Parameter(in = ParameterIn.PATH, description = "캡슐 스킨 아이디", required = true)
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
            description = "처리 완료"
        )
    })
    @GetMapping(
        value = "/search",
        produces = {"application/json"}
    )
    ResponseEntity<ApiSpec<CapsuleSkinSearchPageResponse>> searchCapsuleSkin(
        @Parameter(in = ParameterIn.QUERY, description = "캡슐 스킨 이름", required = true)
        Long capsuleSkinName,

        @Parameter(in = ParameterIn.QUERY, description = "페이지 크기", required = true)
        Long size,

        @Parameter(in = ParameterIn.QUERY, description = "마지막 스킨 아이디", required = true)
        Long capsuleSkinId
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
            description = "ok"
        )
    })
    @GetMapping(
        produces = {"application/json"}
    )
    ResponseEntity<ApiSpec<CapsuleSkinsPageResponse>> getCapsuleSkins(
        Long memberId,

        @Parameter(in = ParameterIn.QUERY, description = "페이지 크기", required = true)
        Long size,

        @Parameter(in = ParameterIn.QUERY, description = "마지막 스킨 생성 시간", required = true)
        ZonedDateTime createdAt
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
        value = "/{capsule_skin_id}",
        consumes = {"application/json"})
    ResponseEntity<ApiSpec<String>> updateCapsuleSkin(
        @Parameter(in = ParameterIn.PATH, description = "캡슐 스킨 아이디", required = true)
        @PathVariable("capsule_skin_id") Long capsuleSkinId
    );
}
