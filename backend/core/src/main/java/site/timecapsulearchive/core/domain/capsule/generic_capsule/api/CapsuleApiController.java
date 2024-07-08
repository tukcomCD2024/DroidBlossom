package site.timecapsulearchive.core.domain.capsule.generic_capsule.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CoordinateRangeDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyARCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.CapsuleOpenedResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.ImagesPageResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.NearbyARCapsuleResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.NearbyCapsuleResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.facade.CapsuleFacade;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.service.CapsuleService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@RestController
@RequestMapping("/capsules")
@RequiredArgsConstructor
public class CapsuleApiController implements CapsuleApi {

    private final CapsuleService capsuleService;
    private final CapsuleFacade capsuleFacade;
    private final GeoTransformManager geoTransformManager;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;

    @GetMapping(value = "/images", produces = {"application/json"})
    @Override
    public ResponseEntity<ImagesPageResponse> getImages(final Long size, final Long capsuleId) {
        return null;
    }

    @GetMapping(value = "/my/ar/nearby", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<NearbyARCapsuleResponse>> getARNearbyMyCapsules(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(value = "latitude") final double latitude,
        @RequestParam(value = "longitude") final double longitude,
        @RequestParam(value = "distance") final double distance,
        @RequestParam(value = "capsule_type", required = false, defaultValue = "ALL") final CapsuleType capsuleType
    ) {
        final List<NearbyARCapsuleSummaryDto> dtos = capsuleService.findARCapsuleByCurrentLocationAndCapsuleType(
            memberId,
            CoordinateRangeDto.from(latitude, longitude, distance),
            capsuleType
        );

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                NearbyARCapsuleResponse.createOf(
                    dtos,
                    geoTransformManager::changePoint3857To4326,
                    s3PreSignedUrlManager::getS3PreSignedUrlForGet
                )
            )
        );
    }

    @GetMapping(value = "/my/map/nearby", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<NearbyCapsuleResponse>> getMapNearbyMyCapsules(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(value = "latitude") final double latitude,
        @RequestParam(value = "longitude") final double longitude,
        @RequestParam(value = "distance") final double distance,
        @RequestParam(value = "capsule_type", required = false, defaultValue = "ALL") final CapsuleType capsuleType
    ) {
        final List<NearbyCapsuleSummaryDto> dtos = capsuleService.findCapsuleByCurrentLocationAndCapsuleType(
            memberId,
            CoordinateRangeDto.from(latitude, longitude, distance),
            capsuleType
        );

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                NearbyCapsuleResponse.createOf(dtos, geoTransformManager::changePoint3857To4326)
            )
        );
    }

    @GetMapping(value = "/friends/map/nearby", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<NearbyCapsuleResponse>> getMapNearbyFriendsCapsules(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(value = "latitude") final double latitude,
        @RequestParam(value = "longitude") final double longitude,
        @RequestParam(value = "distance") final double distance
    ) {
        final List<NearbyCapsuleSummaryDto> capsules = capsuleService.findFriendsCapsulesByCurrentLocation(
            memberId,
            CoordinateRangeDto.from(latitude, longitude, distance)
        );

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                NearbyCapsuleResponse.createOf(capsules, geoTransformManager::changePoint3857To4326)
            )
        );
    }

    @GetMapping(value = "/friends/ar/nearby", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<NearbyARCapsuleResponse>> getARNearbyFriendsCapsules(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(value = "latitude") final double latitude,
        @RequestParam(value = "longitude") final double longitude,
        @RequestParam(value = "distance") final double distance
    ) {
        final List<NearbyARCapsuleSummaryDto> capsules = capsuleService.findFriendsARCapsulesByCurrentLocation(
            memberId,
            CoordinateRangeDto.from(latitude, longitude, distance)
        );

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                NearbyARCapsuleResponse.createOf(
                    capsules,
                    geoTransformManager::changePoint3857To4326,
                    s3PreSignedUrlManager::getS3PreSignedUrlForGet
                )
            )
        );
    }

    @PatchMapping(value = "/{capsule_id}/opened", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<CapsuleOpenedResponse>> updateCapsuleOpened(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("capsule_id") final Long capsuleId
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                capsuleFacade.updateCapsuleOpened(memberId, capsuleId)
            )
        );
    }

}
