package site.timecapsulearchive.core.domain.capsule.secret_capsule.api;

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
import site.timecapsulearchive.core.domain.capsule.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto.SecretCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.reqeust.SecretCapsuleCreateRequest;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.reqeust.SecretCapsuleUpdateRequest;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.MySecretCapsuleSliceResponse;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.SecretCapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.SecretCapsuleSummaryResponse;
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
        @RequestParam(defaultValue = "0", value = "createdAt") final ZonedDateTime createdAt
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
    public ResponseEntity<ApiSpec<SecretCapsuleSummaryResponse>> getSecretCapsuleSummary(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("capsule_id") final Long capsuleId
    ) {
        final SecretCapsuleSummaryDto dto = secretCapsuleService.findSecretCapsuleSummaryById(
            memberId,
            capsuleId);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                capsuleMapper.secretCapsuleSummaryDtoToResponse(dto)
            )
        );
    }

    @GetMapping(value = "/capsules/{capsule_id}/detail", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<SecretCapsuleDetailResponse>> getSecretCapsuleDetail(
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

    @PostMapping(value = "/capsules", consumes = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<String>> createSecretCapsule(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody final SecretCapsuleCreateRequest request
    ) {
        secretCapsuleService.saveCapsule(
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
        @AuthenticationPrincipal final Long memberId,
        @ModelAttribute final SecretCapsuleUpdateRequest request,
        @PathVariable("capsule_id") final Long capsuleId
    ) {
        return null;
    }
}