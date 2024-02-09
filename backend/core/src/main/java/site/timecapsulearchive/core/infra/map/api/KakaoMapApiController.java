package site.timecapsulearchive.core.infra.map.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;
import site.timecapsulearchive.core.infra.map.data.dto.AddressData;
import site.timecapsulearchive.core.infra.map.manager.KakaoMapApiManager;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class KakaoMapApiController implements KakaoMapApi {

    private final KakaoMapApiManager kakaoMapApiManager;

    @GetMapping(value = "/full-address", produces = {"application/json"})
    public ResponseEntity<ApiSpec<AddressData>> getAddressByCoordinate(
        @RequestParam(value = "latitude") final double latitude,
        @RequestParam(value = "longitude") final double longitude
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                kakaoMapApiManager.reverseGeoCoding(latitude, longitude)
            )
        );
    }
}
