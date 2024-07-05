package site.timecapsulearchive.core.domain.capsule.public_capsule.api;


import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsule.data.dto.CapsuleBasicInfoDto;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.request.CapsuleCreateRequest;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.CapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.CapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.facade.CapsuleFacade;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto.MyPublicCapsuleSliceResponse;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto.PublicCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto.PublicCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.reqeust.PublicCapsuleUpdateRequest;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.response.PublicCapsuleSliceResponse;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.response.PublicCapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsule.public_capsule.service.PublicCapsuleService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@RestController
@RequestMapping("/public-capsules")
@RequiredArgsConstructor
public class PublicCapsuleApiController implements PublicCapsuleApi {

    private final PublicCapsuleService publicCapsuleService;
    private final CapsuleFacade capsuleFacade;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;
    private final GeoTransformManager geoTransformManager;


    @PostMapping(consumes = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<String>> createPublicCapsule(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody final CapsuleCreateRequest request) {
        capsuleFacade.saveCapsule(memberId, request.toDto(), CapsuleType.PUBLIC);

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.SUCCESS
            )
        );
    }

    @GetMapping(
        value = "/{capsule_id}/summary",
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<PublicCapsuleSummaryResponse>> getPublicCapsuleSummaryById(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("capsule_id") final Long capsuleId
    ) {
        final PublicCapsuleSummaryDto summaryDto = publicCapsuleService.findPublicCapsuleSummaryByMemberIdAndCapsuleId(
            memberId, capsuleId);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                PublicCapsuleSummaryResponse.createOf(
                    summaryDto,
                    s3PreSignedUrlManager::getS3PreSignedUrlForGet,
                    geoTransformManager::changePoint3857To4326
                )
            )
        );
    }

    @GetMapping(
        value = "/{capsule_id}/detail",
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<CapsuleDetailResponse>> getPublicCapsuleDetailById(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("capsule_id") final Long capsuleId
    ) {
        final CapsuleDetailDto detailDto = publicCapsuleService.findPublicCapsuleDetailByMemberIdAndCapsuleId(
            memberId, capsuleId);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                CapsuleDetailResponse.createOf(
                    detailDto,
                    s3PreSignedUrlManager::getS3PreSignedUrlForGet,
                    s3PreSignedUrlManager::getS3PreSignedUrlsForGet,
                    geoTransformManager::changePoint3857To4326
                )
            )
        );
    }

    @GetMapping(produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<PublicCapsuleSliceResponse>> getPublicCapsules(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(defaultValue = "20", value = "size") final int size,
        @RequestParam(value = "created_at") final ZonedDateTime createdAt
    ) {
        final Slice<PublicCapsuleDetailDto> publicCapsuleSlice = publicCapsuleService.findPublicCapsulesMadeByFriend(
            memberId, size, createdAt);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                PublicCapsuleSliceResponse.createOf(
                    publicCapsuleSlice.getContent(),
                    publicCapsuleSlice.hasNext(),
                    geoTransformManager::changePoint3857To4326,
                    s3PreSignedUrlManager::getS3PreSignedUrlForGet,
                    s3PreSignedUrlManager::getS3PreSignedUrlsForGet
                )
            )
        );
    }

    @GetMapping(value = "/my", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<MyPublicCapsuleSliceResponse>> getMyPublicCapsules(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(defaultValue = "20", value = "size") final int size,
        @RequestParam(value = "created_at") final ZonedDateTime createdAt
    ) {
        final Slice<CapsuleBasicInfoDto> publicCapsules = publicCapsuleService.findMyPublicCapsuleSlice(
            memberId, size, createdAt);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                MyPublicCapsuleSliceResponse.createOf(
                    publicCapsules.getContent(),
                    publicCapsules.hasNext(),
                    s3PreSignedUrlManager::getS3PreSignedUrlForGet
                )
            )
        );
    }

    @Override
    public ResponseEntity<Void> updatePublicCapsuleById(Long capsuleId,
        PublicCapsuleUpdateRequest request) {
        return null;
    }
}
