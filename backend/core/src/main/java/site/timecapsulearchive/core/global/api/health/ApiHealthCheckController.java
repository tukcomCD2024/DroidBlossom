package site.timecapsulearchive.core.global.api.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequestMapping
public class ApiHealthCheckController {

    @Operation(
        summary = "상태 체크 엔드포인트",
        description = "Api의 상태를 반환한다.",
        tags = {"api"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    @GetMapping("/health")
    public ResponseEntity<ApiSpec<HealthResponse>> health() {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                HealthResponse.ok()
            )
        );
    }
}
