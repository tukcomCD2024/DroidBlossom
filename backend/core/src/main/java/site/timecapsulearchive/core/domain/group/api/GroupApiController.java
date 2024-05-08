package site.timecapsulearchive.core.domain.group.api;

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
import site.timecapsulearchive.core.domain.group.data.dto.GroupCreateDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupDetailDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupSummaryDto;
import site.timecapsulearchive.core.domain.group.data.reqeust.GroupCreateRequest;
import site.timecapsulearchive.core.domain.group.data.reqeust.GroupUpdateRequest;
import site.timecapsulearchive.core.domain.group.data.response.GroupDetailResponse;
import site.timecapsulearchive.core.domain.group.data.response.GroupsSliceResponse;
import site.timecapsulearchive.core.domain.group.service.GroupService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;
import site.timecapsulearchive.core.infra.s3.manager.S3UrlGenerator;


@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupApiController implements GroupApi {

    private final GroupService groupService;
    private final S3UrlGenerator s3UrlGenerator;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;

    @Override
    public ResponseEntity<Void> acceptGroupInvitation(Long groupId, Long memberId) {
        return null;
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiSpec<String>> createGroup(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody GroupCreateRequest request
    ) {
        final String groupProfileUrl = s3UrlGenerator.generateFileName(memberId,
            request.groupDirectory(), request.groupImage());
        final GroupCreateDto dto = request.toDto(groupProfileUrl);

        groupService.createGroup(memberId, dto);

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.SUCCESS
            )
        );
    }

    @Override
    public ResponseEntity<Void> deleteGroupById(Long groupId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteGroupMember(Long groupId, Long memberId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> denyGroupInvitation(Long groupId, Long memberId) {
        return null;
    }

    @GetMapping(
        value = "/{group_id}",
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<GroupDetailResponse>> findGroupDetailById(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("group_id") final Long groupId
    ) {
        final GroupDetailDto groupDetailDto = groupService.findGroupDetailByGroupId(memberId,
            groupId);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                groupDetailDto.toResponse(s3PreSignedUrlManager::getS3PreSignedUrlForGet)
            )
        );
    }

    @GetMapping(
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<GroupsSliceResponse>> findGroups(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(defaultValue = "20", value = "size") final int size,
        @RequestParam(value = "created_at") final ZonedDateTime createdAt
    ) {
        final Slice<GroupSummaryDto> groupsSlice = groupService.findGroupsSlice(memberId, size,
            createdAt);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                GroupsSliceResponse.createOf(
                    groupsSlice.getContent(),
                    groupsSlice.hasNext(),
                    s3PreSignedUrlManager::getS3PreSignedUrlForGet
                )
            )
        );
    }

    @PostMapping(value = "/invite/{group_id}/member/{target_id}")
    @Override
    public ResponseEntity<ApiSpec<String>> inviteGroup(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("group_id") final Long groupId,
        @PathVariable("target_id") final Long targetId
    ) {
        groupService.inviteGroup(memberId, groupId, targetId);

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.ACCEPTED
            )
        );
    }

    @Override
    public ResponseEntity<Void> quitGroup(Long groupId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateGroupById(Long groupId, GroupUpdateRequest request) {
        return null;
    }
}
