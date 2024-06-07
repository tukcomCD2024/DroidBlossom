package site.timecapsulearchive.core.domain.capsule.treasure_capsule.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsule.treasure_capsule.service.TreasureCapsuleService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequestMapping("/treasure-capsules")
@RequiredArgsConstructor
public class TreasureCapsuleController implements TreasureCapsuleApi {

    private final TreasureCapsuleService treasureCapsuleService;

    @Override
    @PostMapping("/{capsule_id}/image/{image_id}/open")
    public ResponseEntity<ApiSpec<String>> openTreasureCapsule(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable(value = "capsule_id") final Long capsuleId,
        @PathVariable(value = "image_id") final Long imageId
    ) {
        treasureCapsuleService.openTreasureCapsule(memberId, capsuleId, imageId);

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.SUCCESS
            )
        );
    }
}
