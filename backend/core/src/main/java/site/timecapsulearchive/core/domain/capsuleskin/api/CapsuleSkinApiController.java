package site.timecapsulearchive.core.domain.capsuleskin.api;

import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsuleskin.dto.reqeust.CapsuleSkinCreateRequest;
import site.timecapsulearchive.core.domain.capsuleskin.dto.response.CapsuleSkinSearchPageResponse;
import site.timecapsulearchive.core.domain.capsuleskin.dto.response.CapsuleSkinSummaryResponse;
import site.timecapsulearchive.core.domain.capsuleskin.dto.response.CapsuleSkinsPageResponse;
import site.timecapsulearchive.core.domain.capsuleskin.service.CapsuleSkinService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequestMapping("/capsule-skins")
@RequiredArgsConstructor
public class CapsuleSkinApiController implements CapsuleSkinApi {

    private final CapsuleSkinService capsuleSkinService;

    @Override
    public ResponseEntity<ApiSpec<CapsuleSkinSummaryResponse>> createCapsuleSkin(
        @ModelAttribute CapsuleSkinCreateRequest request
    ) {
        return null;
    }

    @Override
    public ResponseEntity<ApiSpec<String>> deleteCapsuleSkin(Long capsuleSkinId) {
        return null;
    }

    @Override
    public ResponseEntity<ApiSpec<CapsuleSkinSearchPageResponse>> searchCapsuleSkin(
        @RequestParam(value = "capsule_skin_name") Long capsuleSkinName,
        @RequestParam(value = "size") Long size,
        @RequestParam(value = "capsule_skin_id") Long capsuleSkinId
    ) {
        return null;
    }

    @Override
    public ResponseEntity<ApiSpec<CapsuleSkinsPageResponse>> getCapsuleSkins(
        @AuthenticationPrincipal Long memberId,
        @RequestParam(value = "size", defaultValue = "20") int size,
        @RequestParam(value = "createdAt") ZonedDateTime createdAt
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                capsuleSkinService.findCapsuleSkinSliceByCreatedAt(memberId, size, createdAt)
            )
        );
    }

    @Override
    public ResponseEntity<ApiSpec<String>> updateCapsuleSkin(Long capsuleSkinId) {
        return null;
    }
}
