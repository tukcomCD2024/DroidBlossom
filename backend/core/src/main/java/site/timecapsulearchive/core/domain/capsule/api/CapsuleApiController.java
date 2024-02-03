package site.timecapsulearchive.core.domain.capsule.api;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsule.dto.AddressData;
import site.timecapsulearchive.core.domain.capsule.dto.CoordinateRangeRequestDto;
import site.timecapsulearchive.core.domain.capsule.dto.response.CapsuleOpenedResponse;
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

    public ResponseEntity<ApiSpec<AddressData>> getAddressByCoordinate(
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

    @Override
    public ResponseEntity<ApiSpec<CapsuleOpenedResponse>> updateCapsuleOpened(
        @AuthenticationPrincipal Long memberId,
        @NotNull @PathVariable("capsule_id") Long capsuleId
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                capsuleService.updateCapsuleOpened(memberId, capsuleId)
            )
        );
    }
}
