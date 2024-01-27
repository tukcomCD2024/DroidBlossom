package site.timecapsulearchive.core.domain.capsule.api;

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
import org.springframework.web.bind.annotation.RequestParam;
import site.timecapsulearchive.core.domain.capsule.dto.response.ImagesPageResponse;
import site.timecapsulearchive.core.domain.capsule.dto.response.MyCapsulePageResponse;
import site.timecapsulearchive.core.domain.capsule.dto.response.NearbyCapsulePageResponse;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.global.common.response.ApiSpec;

public interface CapsuleApi {

    @Operation(
        summary = "내 캡슐 목록 조회",
        description = "사용자가 생성한 캡슐 목록을 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"capsule"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        )
    })
    @GetMapping(
        produces = {"application/json"}
    )
    ResponseEntity<MyCapsulePageResponse> findCapsulesByMemberId(
        @Parameter(in = ParameterIn.QUERY, description = "페이지 크기", required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "size") Long size,

        @Parameter(in = ParameterIn.QUERY, description = "마지막 캡슐 아이디", required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "capsule_id") Long capsuleId
    );


    @Operation(
        summary = "캡슐에 담겨있는 이미지 목록 조회",
        description = "사용자가 생성한 캡슐에 들어있는 이미지 목록을 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"capsule"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        )
    })
    @GetMapping(
        value = "/capsules/images",
        produces = {"application/json"}
    )
    ResponseEntity<ImagesPageResponse> findImages(
        @Parameter(in = ParameterIn.QUERY, description = "페이지 크기", required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "size") Long size,

        @Parameter(in = ParameterIn.QUERY, description = "마지막 이미지 아이디", required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "capsule_id") Long capsuleId
    );

    @Operation(
        summary = "현재 사용자 위치 기준 캡슐 목록 조회",
        description = "현재 사용자 위치를 바탕으로 반경 distance만큼 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"capsule"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        )
    })
    @GetMapping(
        value = "/capsules/nearby",
        produces = {"application/json"}
    )
    ResponseEntity<ApiSpec<NearbyCapsulePageResponse>> getNearByCapsules(
        Long memberId,

        @Parameter(in = ParameterIn.QUERY, description = "위도", required = true)
        @NotNull @Valid double longitude,

        @Parameter(in = ParameterIn.QUERY, description = "경도", required = true)
        @NotNull @Valid double latitude,

        @Parameter(in = ParameterIn.QUERY, description = "조회 거리", required = true)
        @NotNull @Valid double distance,

        @Parameter(in = ParameterIn.QUERY, description = "캡슐 필터링 타입", schema = @Schema(defaultValue = "ALL"))
        @NotNull @Valid CapsuleType capsuleType
    );
}

