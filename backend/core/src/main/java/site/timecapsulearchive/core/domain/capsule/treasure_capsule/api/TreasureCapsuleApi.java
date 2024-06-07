package site.timecapsulearchive.core.domain.capsule.treasure_capsule.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import site.timecapsulearchive.core.global.common.response.ApiSpec;

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
        )
    })
    ResponseEntity<ApiSpec<String>> openTreasureCapsule(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "개봉할 보물 캡슐 아이디", required = true)
        Long capsuleId,

        @Parameter(in = ParameterIn.PATH, description = "보물 url 저장한 이미지 아이디", required = true)
        Long imageId
    );

}
