package site.timecapsulearchive.core.domain.capsule.secret_capsule.api;

import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.reqeust.SecretCapsuleUpdateRequest;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.CapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.CapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.MySecretCapsuleSliceResponse;
import site.timecapsulearchive.core.domain.capsule.service.SecretCapsuleService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequestMapping("/secret")
@RequiredArgsConstructor
public class SecretCapsuleApiController implements SecretCapsuleApi {

    private final SecretCapsuleService secretCapsuleService;
    private final CapsuleMapper capsuleMapper;

    @GetMapping(value = "/capsules", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<MySecretCapsuleSliceResponse>> getMySecretCapsules(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(defaultValue = "20", value = "size") final int size,
        @RequestParam(defaultValue = "0", value = "created_at") final ZonedDateTime createdAt
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                secretCapsuleService.findSecretCapsuleSliceByMemberId(
                    memberId,
                    size,
                    createdAt
                )
            )
        );
    }

    @GetMapping(value = "/capsules/{capsule_id}/summary", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<CapsuleSummaryResponse>> getSecretCapsuleSummary(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("capsule_id") final Long capsuleId
    ) {
        final CapsuleSummaryDto dto = secretCapsuleService.findSecretCapsuleSummaryById(
            memberId,
            capsuleId);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                capsuleMapper.capsuleSummaryDtoToResponse(dto)
            )
        );
    }

    @GetMapping(value = "/capsules/{capsule_id}/detail", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<CapsuleDetailResponse>> getSecretCapsuleDetail(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("capsule_id") final Long capsuleId
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                secretCapsuleService.findSecretCapsuleDetailById(memberId, capsuleId)
            )
        );
    }

    @PatchMapping(value = "/capsules/{capsule_id}", consumes = {"multipart/form-data"})
    @Override
    public ResponseEntity<CapsuleSummaryResponse> updateSecretCapsule(
        @AuthenticationPrincipal final Long memberId,
        @ModelAttribute final SecretCapsuleUpdateRequest request,
        @PathVariable("capsule_id") final Long capsuleId
    ) {
        return null;
    }
}
