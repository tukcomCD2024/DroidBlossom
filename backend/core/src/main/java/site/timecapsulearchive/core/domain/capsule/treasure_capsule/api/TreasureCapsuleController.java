package site.timecapsulearchive.core.domain.capsule.treasure_capsule.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.dto.TreasureCapsuleOpenDto;
import site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.dto.TreasureCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.response.TreasureCapsuleOpenResponse;
import site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.response.TreasureCapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsule.treasure_capsule.service.TreasureCapsuleService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@RestController
@RequestMapping("/treasure-capsules")
@RequiredArgsConstructor
public class TreasureCapsuleController implements TreasureCapsuleApi {

    private final TreasureCapsuleService treasureCapsuleService;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;

    @Override
    @PostMapping("/{capsule_id}/open")
    public ResponseEntity<ApiSpec<TreasureCapsuleOpenResponse>> openTreasureCapsule(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable(value = "capsule_id") final Long capsuleId
    ) {
        final TreasureCapsuleOpenDto treasureCapsuleOpenDto = treasureCapsuleService.openTreasureCapsule(
            memberId, capsuleId);

        return ResponseEntity.accepted().body(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                TreasureCapsuleOpenResponse.createOf(
                    treasureCapsuleOpenDto,
                    s3PreSignedUrlManager::getS3PreSignedUrlForGet
                )
            )
        );
    }

    @Override
    @GetMapping(
        value = "/{capsule_id}/summary",
        produces = {"application/json"}
    )
    public ResponseEntity<ApiSpec<TreasureCapsuleSummaryResponse>> findTreasureCapsuleSummary(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("capsule_id") final Long capsuleId
    ) {
        final TreasureCapsuleSummaryDto treasureCapsuleSummaryDto = treasureCapsuleService.findTreasureCapsuleSummary(
            capsuleId);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                TreasureCapsuleSummaryResponse.createOf(
                    treasureCapsuleSummaryDto,
                    s3PreSignedUrlManager::getS3PreSignedUrlForGet
                )
            )
        );
    }
}
