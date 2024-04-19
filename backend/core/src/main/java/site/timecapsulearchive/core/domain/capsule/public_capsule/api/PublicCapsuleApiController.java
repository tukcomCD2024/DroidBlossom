package site.timecapsulearchive.core.domain.capsule.public_capsule.api;


import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.CapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.CapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto.PublicCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.reqeust.PublicCapsuleUpdateRequest;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.response.PublicCapsuleSliceResponse;
import site.timecapsulearchive.core.domain.capsule.service.PublicCapsuleService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicCapsuleApiController implements PublicCapsuleApi {

    private final PublicCapsuleService publicCapsuleService;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;
    private final GeoTransformManager geoTransformManager;

    @GetMapping(
        value = "/capsules/{capsule_id}/summary",
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<CapsuleSummaryResponse>> getPublicCapsuleSummaryById(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("capsule_id") final Long capsuleId
    ) {
        final CapsuleSummaryDto summaryDto = publicCapsuleService.findPublicCapsuleSummaryByMemberIdAndCapsuleId(
            memberId, capsuleId);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                CapsuleSummaryResponse.createOf(summaryDto,
                    s3PreSignedUrlManager::getS3PreSignedUrlForGet)
            )
        );
    }

    @GetMapping(
        value = "/capsules/{capsule_id}/detail",
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
                    s3PreSignedUrlManager::getS3PreSignedUrlsForGet
                )
            )
        );
    }

    @GetMapping(value = "/capsules", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<PublicCapsuleSliceResponse>> getPublicCapsules(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(defaultValue = "20", value = "size") final int size,
        @RequestParam(defaultValue = "0", value = "created_at") final ZonedDateTime createdAt
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

    @Override
    public ResponseEntity<Void> updatePublicCapsuleById(Long capsuleId,
        PublicCapsuleUpdateRequest request) {
        return null;
    }
}
