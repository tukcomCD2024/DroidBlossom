package site.timecapsulearchive.core.domain.capsule.public_capsule.api;


import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.reqeust.PublicCapsuleUpdateRequest;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.response.PublicCapsuleSliceResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.CapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.CapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsule.service.PublicCapsuleService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicCapsuleApiController implements PublicCapsuleApi {

    private final PublicCapsuleService publicCapsuleService;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;

    @GetMapping(
        value = "/capsules/{capsule_id}/summary",
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<CapsuleSummaryResponse>> getPublicCapsuleSummaryById(
        @AuthenticationPrincipal Long memberId,
        @PathVariable("capsule_id") Long capsuleId
    ) {
        CapsuleSummaryDto summaryDto = publicCapsuleService.findPublicCapsuleSummaryByMemberIdAndCapsuleId(
            memberId, capsuleId);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                CapsuleSummaryResponse.createOf(summaryDto, s3PreSignedUrlManager::getS3PreSignedUrlForGet)
            )
        );
    }

    @GetMapping(
        value = "/capsules/{capsule_id}/detail",
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<CapsuleDetailResponse>> getPublicCapsuleDetailById(
        @AuthenticationPrincipal Long memberId,
        @PathVariable("capsule_id") Long capsuleId
    ) {
        CapsuleDetailDto detailDto = publicCapsuleService.findPublicCapsuleDetailByMemberIdAndCapsuleId(
            memberId, capsuleId);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                CapsuleDetailResponse.createOf(detailDto, s3PreSignedUrlManager::getS3PreSignedUrlForGet)
            )
        );
    }

    public ResponseEntity<PublicCapsuleSliceResponse> getPublicCapsules(Long size, Long capsuleId) {
        return null;
    }

    @GetMapping(value = "/capsules", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<PublicCapsuleSliceResponse>> getPublicCapsulesMadeByFriend(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(defaultValue = "20", value = "size") final int size,
        @RequestParam(defaultValue = "0", value = "created_at") final ZonedDateTime createdAt
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                publicCapsuleService.findPublicCapsulesMadeByFriend(memberId, size, createdAt)
            )
        );
    }

    @Override
    public ResponseEntity<Void> updatePublicCapsuleById(Long capsuleId,
        PublicCapsuleUpdateRequest request) {
        return null;
    }
}