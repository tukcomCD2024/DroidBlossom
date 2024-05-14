package site.timecapsulearchive.core.domain.capsuleskin.api;

import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsuleskin.data.mapper.CapsuleSkinMapper;
import site.timecapsulearchive.core.domain.capsuleskin.data.reqeust.CapsuleSkinCreateRequest;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinSearchPageResponse;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinStatusResponse;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinsSliceResponse;
import site.timecapsulearchive.core.domain.capsuleskin.service.CapsuleSkinService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequestMapping("/capsule-skins")
@RequiredArgsConstructor
public class CapsuleSkinApiController implements CapsuleSkinApi {

    private final CapsuleSkinService capsuleSkinService;
    private final CapsuleSkinMapper capsuleSkinMapper;

    @GetMapping(value = "/search", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<CapsuleSkinSearchPageResponse>> searchCapsuleSkins(
        @RequestParam(value = "capsule_skin_name") final Long capsuleSkinName,
        @RequestParam(value = "size") final Long size,
        @RequestParam(value = "capsule_skin_id") final Long capsuleSkinId
    ) {
        return null;
    }

    @GetMapping(produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<CapsuleSkinsSliceResponse>> getCapsuleSkins(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(value = "size", defaultValue = "20") final int size,
        @RequestParam(value = "created_at") final ZonedDateTime createdAt
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

    @PostMapping(produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<CapsuleSkinStatusResponse>> createCapsuleSkin(
        @AuthenticationPrincipal final Long memberId,
        @RequestBody final CapsuleSkinCreateRequest request
    ) {
        return ResponseEntity.accepted().body(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                capsuleSkinService.sendCapsuleSkinCreateMessage(
                    memberId, capsuleSkinMapper.createRequestToDto(request)
                )
            )
        );
    }

    @PatchMapping(value = "/{capsule_skin_id}", consumes = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<String>> updateCapsuleSkin(
        @PathVariable(name = "capsule_skin_id") final Long capsuleSkinId
    ) {
        return null;
    }

    @DeleteMapping(value = "/{capsule_skin_id}")
    @Override
    public ResponseEntity<ApiSpec<String>> deleteCapsuleSkin(
        @PathVariable(name = "capsule_skin_id") final Long capsuleSkinId
    ) {
        return null;
    }
}
