package site.timecapsulearchive.core.domain.capsule.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsule.dto.CoordinateRangeRequestDto;
import site.timecapsulearchive.core.domain.capsule.dto.response.ImagesPageResponse;
import site.timecapsulearchive.core.domain.capsule.dto.response.MyCapsulePageResponse;
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
    public ResponseEntity<ApiSpec<MyCapsulePageResponse>> findCapsulesByMemberId(
        @AuthenticationPrincipal Long memberId,
        @NotNull @Valid @RequestParam(defaultValue = "0", value = "page") int page,
        @NotNull @Valid @RequestParam(defaultValue = "20", value = "size") int size,
        @NotNull @Valid @RequestParam(defaultValue = "0", value = "capsuleId") Long capsuleId
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                capsuleService.findCapsuleListByMemberId(
                    memberId,
                    PageRequest.of(page, size),
                    capsuleId
                )
            )
        );
    }

    @Override
    public ResponseEntity<ImagesPageResponse> findImages(Long size, Long capsuleId) {
        return null;
    }

    @Override
    public ResponseEntity<ApiSpec<NearbyCapsuleResponse>> getNearByCapsules(
        @AuthenticationPrincipal Long memberId,
        @NotNull @Valid @RequestParam(value = "latitude") double latitude,
        @NotNull @Valid @RequestParam(value = "longitude") double longitude,
        @NotNull @Valid @RequestParam(value = "distance") double distance,
        @NotNull @Valid @RequestParam(value = "capsule_type", required = false, defaultValue = "ALL"
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
}
