package site.timecapsulearchive.core.domain.capsule.api.secret_c;

import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsule.data.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.data.secret_c.SecretCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.data.secret_c.reqeust.SecretCapsuleCreateRequest;
import site.timecapsulearchive.core.domain.capsule.data.secret_c.reqeust.SecretCapsuleUpdateRequest;
import site.timecapsulearchive.core.domain.capsule.data.secret_c.response.MySecretCapsulePageResponse;
import site.timecapsulearchive.core.domain.capsule.data.secret_c.response.SecretCapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.data.secret_c.response.SecretCapsuleSummaryResponse;
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
    public ResponseEntity<ApiSpec<MySecretCapsulePageResponse>> getMySecretCapsules(
        @AuthenticationPrincipal Long memberId,
        @RequestParam(defaultValue = "20", value = "size") int size,
        @RequestParam(defaultValue = "0", value = "createdAt") ZonedDateTime createdAt
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                secretCapsuleService.findSecretCapsuleListByMemberId(
                    memberId,
                    size,
                    createdAt
                )
            )
        );
    }

    @GetMapping(value = "/capsules/{capsule_id}/detail", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<SecretCapsuleDetailResponse>> getSecretCapsuleDetail(
        @AuthenticationPrincipal Long memberId,
        @PathVariable("capsule_id") Long capsuleId
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                secretCapsuleService.findSecretCapsuleDetailById(memberId, capsuleId)
            )
        );
    }

    @GetMapping(value = "/capsules/{capsule_id}/summary", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<SecretCapsuleSummaryResponse>> getSecretCapsuleSummary(
        @AuthenticationPrincipal Long memberId,
        @PathVariable("capsule_id") Long capsuleId
    ) {
        SecretCapsuleSummaryDto dto = secretCapsuleService.findSecretCapsuleSummaryById(memberId,
            capsuleId);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                capsuleMapper.secretCapsuleSummaryDtoToResponse(dto)
            )
        );
    }

    @PostMapping(value = "/capsules", consumes = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<String>> createSecretCapsule(
        @AuthenticationPrincipal Long memberId,
        @Valid @RequestBody SecretCapsuleCreateRequest request
    ) {
        secretCapsuleService.createCapsule(
            memberId,
            capsuleMapper.secretCapsuleCreateRequestToDto(request)
        );

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.SUCCESS
            )
        );
    }

    @PatchMapping(value = "/capsules/{capsule_id}", consumes = {"multipart/form-data"})
    @Override
    public ResponseEntity<SecretCapsuleSummaryResponse> updateSecretCapsule(
        @AuthenticationPrincipal Long capsuleId,
        @ModelAttribute SecretCapsuleUpdateRequest request
    ) {
        return null;
    }
}
