package site.timecapsulearchive.core.domain.group.api.query;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.group.data.dto.CompleteGroupSummaryDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupDetailTotalDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberDto;
import site.timecapsulearchive.core.domain.group.data.response.GroupDetailResponse;
import site.timecapsulearchive.core.domain.group.data.response.GroupMemberInfosResponse;
import site.timecapsulearchive.core.domain.group.data.response.GroupsSliceResponse;
import site.timecapsulearchive.core.domain.group.service.query.GroupQueryService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupQueryApiController implements GroupQueryApi {

    private final GroupQueryService groupQueryService;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;

    @GetMapping(
        value = "/{group_id}/detail",
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<GroupDetailResponse>> findGroupDetailById(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("group_id") final Long groupId
    ) {
        final GroupDetailTotalDto groupDetailTotalDto = groupQueryService.findGroupDetailByGroupId(
            memberId,
            groupId);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                groupDetailTotalDto.toResponse(s3PreSignedUrlManager::getS3PreSignedUrlForGet)
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
        final Slice<CompleteGroupSummaryDto> groupsSlice = groupQueryService.findGroupsSlice(memberId,
            size,
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

    @GetMapping(
        value = "/{group_id}/members",
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<GroupMemberInfosResponse>> findGroupMemberInfos(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("group_id") final Long groupId
    ) {
        final List<GroupMemberDto> groupMemberDtos = groupQueryService.findGroupMemberInfos(
            memberId, groupId);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                GroupMemberInfosResponse.createOf(
                    groupMemberDtos,
                    s3PreSignedUrlManager::getS3PreSignedUrlForGet
                )
            )
        );
    }
}
