package site.timecapsulearchive.core.domain.capsule.api;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsule.CoordinateRangeRequestDto;
import site.timecapsulearchive.core.domain.capsule.dto.response.ImagesPageResponse;
import site.timecapsulearchive.core.domain.capsule.dto.response.MyCapsulePageResponse;
import site.timecapsulearchive.core.domain.capsule.dto.response.NearbyCapsulePageResponse;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.service.CapsuleService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@Validated
@RestController
@RequestMapping("/capsules")
@RequiredArgsConstructor
public class CapsuleApiController implements CapsuleApi {

    private final CapsuleService capsuleService;

    @Override
    public ResponseEntity<MyCapsulePageResponse> findCapsulesByMemberId(Long size, Long capsuleId) {
        return null;
    }

    @Override
    public ResponseEntity<ImagesPageResponse> findImages(Long size, Long capsuleId) {
        return null;
    }

    @Override
    public ResponseEntity<ApiSpec<NearbyCapsulePageResponse>> getNearByCapsules(
        @AuthenticationPrincipal Long memberId,
        @NotNull @RequestParam(value = "latitude") final double latitude,
        @NotNull @RequestParam(value = "longitude") final double longitude,
        @NotNull @RequestParam(value = "distance") final double distance,
        @NotNull @RequestParam(value = "capsule_type", required = false, defaultValue = "ALL") final CapsuleType capsuleType
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
}
