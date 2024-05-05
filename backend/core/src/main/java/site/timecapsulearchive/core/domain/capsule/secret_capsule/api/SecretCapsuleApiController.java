package site.timecapsulearchive.core.domain.capsule.secret_capsule.api;

import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
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
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.request.CapsuleCreateRequest;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.CapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.CapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.facade.CapsuleFacade;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto.MySecreteCapsuleDto;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.reqeust.SecretCapsuleUpdateRequest;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.MySecretCapsuleSliceResponse;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.service.SecretCapsuleService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@RestController
@RequestMapping("/secret-capsules")
@RequiredArgsConstructor
public class SecretCapsuleApiController implements SecretCapsuleApi {

    private final SecretCapsuleService secretCapsuleService;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;
    private final CapsuleFacade capsuleFacade;


    @PostMapping(consumes = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<String>> createSecretCapsule(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody final CapsuleCreateRequest request
    ) {
        capsuleFacade.saveCapsule(memberId, request.toDto(), CapsuleType.SECRET);

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.SUCCESS
            )
        );
    }

    @GetMapping(produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<MySecretCapsuleSliceResponse>> getMySecretCapsules(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(defaultValue = "20", value = "size") final int size,
        @RequestParam(defaultValue = "0", value = "created_at") final ZonedDateTime createdAt
    ) {
        final Slice<MySecreteCapsuleDto> dtos = secretCapsuleService.findSecretCapsuleSliceByMemberId(
            memberId, size, createdAt);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                MySecretCapsuleSliceResponse.createOf(
                    dtos, s3PreSignedUrlManager::getS3PreSignedUrlForGet)
            )
        );
    }

    @GetMapping(value = "/{capsule_id}/summary", produces = {"application/json"})
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
                CapsuleSummaryResponse.createOf(dto, s3PreSignedUrlManager::getS3PreSignedUrlForGet)
            )
        );
    }

    @GetMapping(value = "/{capsule_id}/detail", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<CapsuleDetailResponse>> getSecretCapsuleDetail(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("capsule_id") final Long capsuleId
    ) {
        final CapsuleDetailDto dto = secretCapsuleService.findSecretCapsuleDetailById(
            memberId, capsuleId);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                CapsuleDetailResponse.createOf(
                    dto,
                    s3PreSignedUrlManager::getS3PreSignedUrlForGet,
                    s3PreSignedUrlManager::getS3PreSignedUrlsForGet
                )
            )
        );
    }

    @PatchMapping(value = "/{capsule_id}", consumes = {"multipart/form-data"})
    @Override
    public ResponseEntity<CapsuleSummaryResponse> updateSecretCapsule(
        @AuthenticationPrincipal final Long memberId,
        @ModelAttribute final SecretCapsuleUpdateRequest request,
        @PathVariable("capsule_id") final Long capsuleId
    ) {
        return null;
    }
}
