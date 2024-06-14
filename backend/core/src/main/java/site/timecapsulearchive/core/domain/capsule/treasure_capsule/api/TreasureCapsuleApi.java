package site.timecapsulearchive.core.domain.capsule.treasure_capsule.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.response.TreasureCapsuleOpenResponse;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.error.ErrorResponse;

public interface TreasureCapsuleApi {

    @Operation(
        summary = "보물 캡슐 개봉",
        description = "사용자는 보물 캡슐을 개봉할 수 있다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"secret capsule"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202",
            description = "처리 시작"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "보물 캡슐 찾기 실패 예외",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "외부 API 요청 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<ApiSpec<TreasureCapsuleOpenResponse>> openTreasureCapsule(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "개봉할 보물 캡슐 아이디", required = true)
        Long capsuleId
    );

}
