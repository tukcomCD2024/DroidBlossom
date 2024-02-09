package site.timecapsulearchive.core.infra.map.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.infra.map.data.dto.AddressData;

public interface KakaoMapApi {

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
}
