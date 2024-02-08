package site.timecapsulearchive.core.domain.capsuleskin.api;

import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsuleskin.data.reqeust.CapsuleSkinCreateRequest;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinSearchPageResponse;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinSummaryResponse;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinsPageResponse;
import site.timecapsulearchive.core.domain.capsuleskin.service.CapsuleSkinService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequestMapping("/capsule-skins")
@RequiredArgsConstructor
public class CapsuleSkinApiController implements CapsuleSkinApi {

    private final CapsuleSkinService capsuleSkinService;

    @GetMapping(value = "/search", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<CapsuleSkinSearchPageResponse>> searchCapsuleSkin(
        @RequestParam(value = "capsule_skin_name") Long capsuleSkinName,
        @RequestParam(value = "size") Long size,
        @RequestParam(value = "capsule_skin_id") Long capsuleSkinId
    ) {
        return null;
    }

    @GetMapping(produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<CapsuleSkinsPageResponse>> getCapsuleSkins(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(value = "size", defaultValue = "20") final int size,
        @RequestParam(value = "createdAt") final ZonedDateTime createdAt
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                capsuleSkinService.findCapsuleSkinSliceByCreatedAtAndMemberId(
                    memberId,
                    size,
                    createdAt
                )
            )
        );
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @Override
    public ResponseEntity<ApiSpec<CapsuleSkinSummaryResponse>> createCapsuleSkin(
        @ModelAttribute CapsuleSkinCreateRequest request
    ) {
        return null;
    }

    @PatchMapping(value = "/{capsule_skin_id}", consumes = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<String>> updateCapsuleSkin(Long capsuleSkinId) {
        return null;
    }

    @DeleteMapping(value = "/{capsule_skin_id}")
    @Override
    public ResponseEntity<ApiSpec<String>> deleteCapsuleSkin(Long capsuleSkinId) {
        return null;
    }
}
