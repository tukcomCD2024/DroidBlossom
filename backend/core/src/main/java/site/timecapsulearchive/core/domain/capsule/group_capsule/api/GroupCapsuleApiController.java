package site.timecapsulearchive.core.domain.capsule.group_capsule.api;

import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsule.data.dto.CapsuleBasicInfoDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleOpenStateDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleSliceRequestDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.reqeust.GroupCapsuleCreateRequest;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.reqeust.GroupCapsuleUpdateRequest;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleOpenStateResponse;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.MyGroupCapsuleSliceResponse;
import site.timecapsulearchive.core.domain.capsule.group_capsule.facade.GroupCapsuleFacade;
import site.timecapsulearchive.core.domain.capsule.group_capsule.service.GroupCapsuleService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@RestController
@RequestMapping("/group-capsules")
@RequiredArgsConstructor
public class GroupCapsuleApiController implements GroupCapsuleApi {

    private final GroupCapsuleFacade groupCapsuleFacade;
    private final GroupCapsuleService groupCapsuleService;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;
    private final GeoTransformManager geoTransformManager;

    @PostMapping(
        value = "/{group_id}",
        consumes = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<String>> createGroupCapsule(
        @AuthenticationPrincipal Long memberId,
        @PathVariable("group_id") Long groupId,
        @Valid @RequestBody GroupCapsuleCreateRequest request
    ) {
        groupCapsuleFacade.saveGroupCapsule(request.toGroupCapsuleCreateRequestDto(),
            memberId, groupId);

        return ResponseEntity.ok(
            ApiSpec.empty(SuccessCode.SUCCESS)
        );
    }

    @GetMapping(
        value = "/{capsule_id}/detail",
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<GroupCapsuleDetailResponse>> getGroupCapsuleDetailByCapsuleId(
        @AuthenticationPrincipal Long memberId,
        @PathVariable("capsule_id") Long capsuleId
    ) {
        final GroupCapsuleDetailDto detailDto = groupCapsuleService.findGroupCapsuleDetailByGroupIdAndCapsuleId(
            capsuleId);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                GroupCapsuleDetailResponse.createOf(
                    detailDto,
                    s3PreSignedUrlManager::getS3PreSignedUrlForGet,
                    s3PreSignedUrlManager::getS3PreSignedUrlsForGet,
                    geoTransformManager::changePoint3857To4326
                )
            )
        );
    }

    @GetMapping(
        value = "/{capsule_id}/summary",
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<GroupCapsuleSummaryResponse>> getGroupCapsuleSummaryByCapsuleId(
        @AuthenticationPrincipal Long memberId,
        @PathVariable("capsule_id") Long capsuleId
    ) {
        final GroupCapsuleSummaryDto summaryDto = groupCapsuleService.findGroupCapsuleSummaryByGroupIDAndCapsuleId(
            capsuleId);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                GroupCapsuleSummaryResponse.createOf(
                    summaryDto,
                    s3PreSignedUrlManager::getS3PreSignedUrlForGet,
                    geoTransformManager::changePoint3857To4326
                )
            )
        );
    }

    @GetMapping(
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<Slice<CapsuleBasicInfoDto>>> getGroupCapsules(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam("group_id") final Long groupId,
        @RequestParam("size") final int size,
        @RequestParam("last_capsule_id") final Long lastCapsuleId
    ) {
        final GroupCapsuleSliceRequestDto dto = GroupCapsuleSliceRequestDto.createOf(memberId, groupId,
            size, lastCapsuleId);

        final Slice<CapsuleBasicInfoDto> groupCapsuleSlice = groupCapsuleService.findGroupCapsuleSlice(
            dto);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                groupCapsuleSlice
            )
        );
    }

    @GetMapping(value = "/my", produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<MyGroupCapsuleSliceResponse>> getMyGroupCapsules(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(defaultValue = "20", value = "size") final int size,
        @RequestParam(value = "created_at") final ZonedDateTime createdAt
    ) {
        final Slice<CapsuleBasicInfoDto> groupCapsules = groupCapsuleService.findMyGroupCapsuleSlice(
            memberId,
            size,
            createdAt
        );

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                MyGroupCapsuleSliceResponse.createOf(
                    groupCapsules.getContent(),
                    groupCapsules.hasNext(),
                    s3PreSignedUrlManager::getS3PreSignedUrlForGet
                )
            )
        );
    }

    @PostMapping("/{capsule_id}/open")
    @Override
    public ResponseEntity<ApiSpec<GroupCapsuleOpenStateResponse>> openCapsule(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable(value = "capsule_id") final Long capsuleId
    ) {
        GroupCapsuleOpenStateDto groupCapsuleOpenStateDto = groupCapsuleService.openGroupCapsule(
            memberId, capsuleId);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                groupCapsuleOpenStateDto.toResponse()
            )
        );
    }

    @Override
    public ResponseEntity<GroupCapsuleSummaryResponse> updateGroupCapsuleByIdAndGroupId(
        Long groupId, Long capsuleId, GroupCapsuleUpdateRequest request) {
        return null;
    }
}
