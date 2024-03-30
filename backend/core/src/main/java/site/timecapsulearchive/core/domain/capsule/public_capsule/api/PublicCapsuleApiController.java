package site.timecapsulearchive.core.domain.capsule.public_capsule.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.reqeust.PublicCapsuleCreateRequest;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.reqeust.PublicCapsuleUpdateRequest;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.response.PublicCapsulePageResponse;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.response.PublicCapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.CapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.CapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsule.service.PublicCapsuleService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicCapsuleApiController implements PublicCapsuleApi {

    private final PublicCapsuleService publicCapsuleService;

    @Override
    public ResponseEntity<PublicCapsuleSummaryResponse> createPublicCapsule(
        PublicCapsuleCreateRequest request) {
        return null;
    }

    @GetMapping(
        value = "/capsules/{capsule_id}/summary",
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<CapsuleSummaryResponse>> getPublicCapsuleSummaryById(
        @AuthenticationPrincipal Long memberId,
        @PathVariable("capsule_id") Long capsuleId
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                publicCapsuleService.findPublicCapsuleSummaryByMemberIdAndCapsuleId(memberId,
                    capsuleId)
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
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                publicCapsuleService.findPublicCapsuleDetailByMemberIdAndCapsuleId(memberId,
                    capsuleId)
            )
        );
    }

    @Override
    public ResponseEntity<PublicCapsulePageResponse> getPublicCapsules(Long size, Long capsuleId) {
        return null;
    }

    @Override
    public ResponseEntity<PublicCapsuleSummaryResponse> updatePublicCapsuleById(Long capsuleId,
        PublicCapsuleUpdateRequest request) {
        return null;
    }
}
