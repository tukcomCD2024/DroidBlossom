package site.timecapsulearchive.core.domain.capsule.generic_capsule.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.AddressData;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.CapsuleOpenedResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.ImagesPageResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.NearbyCapsuleResponse;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.error.ErrorResponse;

public interface CapsuleApi {

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
    ResponseEntity<ImagesPageResponse> getImages(
        @Parameter(in = ParameterIn.QUERY, description = "페이지 크기", required = true)
        Long size,

        @Parameter(in = ParameterIn.QUERY, description = "마지막 이미지 아이디", required = true)
        Long capsuleId
    );

    @Operation(
        summary = "현재 사용자 위치 기준 캡슐 목록 조회",
        description = "현재 사용자 위치를 바탕으로 반경 distance km만큼 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"capsule"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 파라미터에 의해 발생"
        )
    })
    ResponseEntity<ApiSpec<NearbyCapsuleResponse>> getNearbyCapsules(
        Long memberId,

        @Parameter(in = ParameterIn.QUERY, description = "위도(wsg84)", required = true)
        double longitude,

        @Parameter(in = ParameterIn.QUERY, description = "경도(wsg84)", required = true)
        double latitude,

        @Parameter(in = ParameterIn.QUERY, description = "조회 거리(km)", required = true)
        double distance,

        @Parameter(in = ParameterIn.QUERY, description = "캡슐 필터링 타입", schema = @Schema(defaultValue = "ALL"))
        CapsuleType capsuleType
    );

    @Operation(
        summary = "좌표에 따른 전체 주소 반환",
        description = "위도와 경도 요청시 전체 주소를 반환해 준다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"capsule"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "카카오 맵 API 주소 요청에 실패 했을 경우 발생한다."
        )
    })
    ResponseEntity<ApiSpec<AddressData>> getAddressByCoordinate(
        @Parameter(in = ParameterIn.QUERY, description = "위도(wsg84)", required = true)
        double latitude,

        @Parameter(in = ParameterIn.QUERY, description = "경도(wsg84)", required = true)
        double longitude
    );

    @Operation(
        summary = "캡슐 열람 상태로 변경",
        description = "캡슐을 열람 상태를 변경해준다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"capsule"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "해당 캡슐을 찾을 수 없을 경우 발생하는 예외",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<ApiSpec<CapsuleOpenedResponse>> updateCapsuleOpened(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "캡슐 아이디", required = true)
        Long capsuleId
    );
}

