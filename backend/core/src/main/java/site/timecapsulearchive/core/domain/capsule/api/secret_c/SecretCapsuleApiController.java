package site.timecapsulearchive.core.domain.capsule.api.secret_c;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsule.dto.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.reqeust.SecretCapsuleCreateRequest;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.reqeust.SecretCapsuleUpdateRequest;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.response.SecretCapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.response.SecretCapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsule.service.SecreteCapsuleService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequestMapping("/secrete")
@RequiredArgsConstructor
public class SecretCapsuleApiController implements SecretCapsuleApi {

    private final SecreteCapsuleService secreteCapsuleService;
    private final CapsuleMapper capsuleMapper;

    @Override
    public ResponseEntity<ApiSpec<String>> createSecretCapsule(
        @AuthenticationPrincipal Long memberId,
        @RequestBody SecretCapsuleCreateRequest request
    ) {
        secreteCapsuleService.createCapsule(
            memberId,
            capsuleMapper.secretCapsuleCreateRequestToDto(request)
        );

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.SUCCESS
            )
        );
    }

    @Override
    public ResponseEntity<ApiSpec<SecretCapsuleDetailResponse>> findSecretCapsuleDetailById(
        @AuthenticationPrincipal Long memberId,
        @NotNull @PathVariable("capsule_id") Long capsuleId) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                secreteCapsuleService.findSecretCapsuleDetailById(memberId, capsuleId)
            )
        );
    }

    public ResponseEntity<ApiSpec<SecretCapsuleSummaryResponse>> findSecretCapsuleSummary(
        @AuthenticationPrincipal Long memberId,
        @NotNull @PathVariable("capsule_id") Long capsuleId
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                secreteCapsuleService.findSecretCapsuleSummaryById(memberId, capsuleId)
            )
        );
    }

    @Override
    public ResponseEntity<SecretCapsuleSummaryResponse> updateSecretCapsule(Long capsuleId,
        SecretCapsuleUpdateRequest request) {
        return null;
    }
}
