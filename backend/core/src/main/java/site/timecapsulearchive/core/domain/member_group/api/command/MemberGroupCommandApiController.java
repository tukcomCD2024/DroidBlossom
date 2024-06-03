package site.timecapsulearchive.core.domain.member_group.api.command;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.member_group.data.request.SendGroupRequest;
import site.timecapsulearchive.core.domain.member_group.facade.MemberGroupFacade;
import site.timecapsulearchive.core.domain.member_group.service.MemberGroupCommandService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class MemberGroupCommandApiController implements MemberGroupCommandApi {

    private final MemberGroupCommandService memberGroupCommandService;
    private final MemberGroupFacade memberGroupFacade;

    @DeleteMapping(value = "/{group_id}/members/quit")
    @Override
    public ResponseEntity<ApiSpec<String>> quitGroup(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("group_id") final Long groupId
    ) {
        memberGroupCommandService.quitGroup(memberId, groupId);

        return ResponseEntity.ok(ApiSpec.empty(SuccessCode.SUCCESS));
    }

    @PostMapping(value = "/invite")
    @Override
    public ResponseEntity<ApiSpec<String>> inviteGroup(
        @AuthenticationPrincipal final Long memberId,
        @RequestBody final SendGroupRequest sendGroupRequest
    ) {
        memberGroupCommandService.inviteGroup(memberId, sendGroupRequest);

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.ACCEPTED
            )
        );
    }

    @PostMapping(value = "/{group_id}/accept")
    @Override
    public ResponseEntity<ApiSpec<String>> acceptGroupInvitation(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("group_id") final Long groupId
    ) {
        memberGroupFacade.acceptGroupInvite(memberId, groupId);

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.ACCEPTED
            )
        );
    }

    @DeleteMapping(value = "/{group_id}/member/{target_id}/reject")
    public ResponseEntity<ApiSpec<String>> rejectGroupInvitation(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("group_id") final Long groupId,
        @PathVariable("target_id") final Long targetId) {

        memberGroupCommandService.rejectRequestGroup(memberId, groupId, targetId);

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.SUCCESS
            )
        );
    }

    @DeleteMapping(value = "/{group_id}/members/{group_member_id}")
    @Override
    public ResponseEntity<ApiSpec<String>> kickGroupMember(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("group_id") final Long groupId,
        @PathVariable("group_member_id") final Long groupMemberId
    ) {
        memberGroupCommandService.kickGroupMember(memberId, groupId, groupMemberId);

        return ResponseEntity.ok(ApiSpec.empty(SuccessCode.SUCCESS));
    }
}
