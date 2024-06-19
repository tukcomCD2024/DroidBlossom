package site.timecapsulearchive.core.domain.member_group.api.query;

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
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberDto;
import site.timecapsulearchive.core.domain.group.data.response.GroupMemberInfosResponse;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupInviteSummaryDto;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupSendingInviteMemberDto;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupSendingInvitesSliceRequestDto;
import site.timecapsulearchive.core.domain.member_group.data.response.GroupReceivingInvitesSliceResponse;
import site.timecapsulearchive.core.domain.member_group.data.response.GroupSendingInvitesSliceResponse;
import site.timecapsulearchive.core.domain.member_group.service.MemberGroupQueryService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class MemberGroupQueryApiController implements MemberGroupQueryApi {

    private final MemberGroupQueryService memberGroupQueryService;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;


    @GetMapping(
        value = "/receiving-invites",
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<GroupReceivingInvitesSliceResponse>> findGroupReceivingInvites(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(defaultValue = "20", value = "size") final int size,
        @RequestParam(value = "created_at") final ZonedDateTime createdAt
    ) {
        final Slice<GroupInviteSummaryDto> groupReceivingInvitesSlice = memberGroupQueryService.findGroupReceivingInvitesSlice(
            memberId, size, createdAt);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                GroupReceivingInvitesSliceResponse.createOf(
                    groupReceivingInvitesSlice.getContent(),
                    groupReceivingInvitesSlice.hasNext(),
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
        final List<GroupMemberDto> groupMemberDtos = memberGroupQueryService.findGroupMemberInfos(
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

    @GetMapping(
        value = "/{group_id}/sending-invites",
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<GroupSendingInvitesSliceResponse>> findGroupSendingInvites(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable(value = "group_id") final Long groupId,
        @RequestParam(value = "group_invite_id", required = false) final Long groupInviteId,
        @RequestParam(value = "size") final int size
    ) {
        GroupSendingInvitesSliceRequestDto dto = GroupSendingInvitesSliceRequestDto.create(
            memberId, groupId, groupInviteId, size);

        Slice<GroupSendingInviteMemberDto> groupSendingInvitesSlice = memberGroupQueryService.findGroupSendingInvites(
            dto);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                GroupSendingInvitesSliceResponse.createOf(
                    groupSendingInvitesSlice.getContent(),
                    groupSendingInvitesSlice.hasNext()
                )
            )
        );
    }
}
