package site.timecapsulearchive.core.domain.capsule.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsule.dto.CoordinateRangeRequestDto;
import site.timecapsulearchive.core.domain.capsule.dto.response.AddressResponse;
import site.timecapsulearchive.core.domain.capsule.dto.response.ImagesPageResponse;
import site.timecapsulearchive.core.domain.capsule.dto.response.NearbyCapsuleResponse;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.service.CapsuleService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequestMapping("/capsules")
@RequiredArgsConstructor
public class CapsuleApiController implements CapsuleApi {

    private final CapsuleService capsuleService;

    @Override
    public ResponseEntity<ImagesPageResponse> findImages(Long size, Long capsuleId) {
        return null;
    }

    @Override
    public ResponseEntity<ApiSpec<NearbyCapsuleResponse>> getNearByCapsules(
        @AuthenticationPrincipal Long memberId,
        @RequestParam(value = "latitude") double latitude,
        @RequestParam(value = "longitude") double longitude,
        @RequestParam(value = "distance") double distance,
        @RequestParam(value = "capsule_type", required = false, defaultValue = "ALL"
        ) CapsuleType capsuleType
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                capsuleService.findCapsuleByCurrentLocationAndCapsuleType(
                    memberId,
                    CoordinateRangeRequestDto.from(latitude, longitude, distance),
                    capsuleType
                )
            )
        );
    }

    public ResponseEntity<ApiSpec<AddressResponse>> getAddressByCoordinate(
        @RequestParam(value = "latitude") double latitude,
        @RequestParam(value = "longitude") double longitude
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                capsuleService.getFullAddressByCoordinate(latitude, longitude)
            )
        );
    }
}
