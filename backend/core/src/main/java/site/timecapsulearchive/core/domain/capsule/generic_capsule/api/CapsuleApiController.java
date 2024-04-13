package site.timecapsulearchive.core.domain.capsule.generic_capsule.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CoordinateRangeDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.CapsuleOpenedResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.ImagesPageResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.NearbyCapsuleResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.reqeust.CapsuleCreateRequest;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.service.CapsuleService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequestMapping("/capsules")
@RequiredArgsConstructor
public class CapsuleApiController implements CapsuleApi {

    private final CapsuleService capsuleService;
    private final CapsuleMapper capsuleMapper;

    @GetMapping(value = "/images", produces = {"application/json"})
    @Override
    public ResponseEntity<ImagesPageResponse> getImages(final Long size, final Long capsuleId) {
        return null;
    }

    @GetMapping(value = "/nearby", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<NearbyCapsuleResponse>> getNearbyCapsules(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(value = "latitude") final double latitude,
        @RequestParam(value = "longitude") final double longitude,
        @RequestParam(value = "distance") final double distance,
        @RequestParam(value = "capsule_type", required = false, defaultValue = "ALL") final CapsuleType capsuleType
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                capsuleService.findCapsuleByCurrentLocationAndCapsuleType(
                    memberId,
                    CoordinateRangeDto.from(latitude, longitude, distance),
                    capsuleType
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
                capsuleService.updateCapsuleOpened(memberId, capsuleId)
            )
        );
    }

    @PostMapping(value = "/secret", consumes = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<String>> createSecretCapsule(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody final CapsuleCreateRequest request
    ) {
        capsuleService.saveCapsule(
            memberId,
            capsuleMapper.capsuleCreateRequestToDto(request),
            CapsuleType.SECRET
        );

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.SUCCESS
            )
        );
    }

    @PostMapping(value = "/public", consumes = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<String>> createPublicCapsule(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody final CapsuleCreateRequest request) {

        capsuleService.saveCapsule(
            memberId,
            capsuleMapper.capsuleCreateRequestToDto(request),
            CapsuleType.PUBLIC
        );

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.SUCCESS
            )
        );
    }
}
