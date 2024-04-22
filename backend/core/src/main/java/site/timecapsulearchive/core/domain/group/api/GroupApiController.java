package site.timecapsulearchive.core.domain.group.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.group.data.reqeust.GroupCreateRequest;
import site.timecapsulearchive.core.domain.group.data.reqeust.GroupUpdateRequest;
import site.timecapsulearchive.core.domain.group.data.response.GroupDetailResponse;
import site.timecapsulearchive.core.domain.group.data.response.GroupsPageResponse;
import site.timecapsulearchive.core.domain.group.service.GroupService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;
import site.timecapsulearchive.core.infra.s3.data.dto.S3Directory;


@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupApiController implements GroupApi {

    private final GroupService groupService;

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
        final String fullPath = S3Directory.GROUP.generateFullPath(memberId, request.groupImage());
        groupService.createGroup(memberId, request.toDto(fullPath));

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

    @Override
    public ResponseEntity<GroupDetailResponse> findGroupById(Long groupId) {
        return null;
    }

    @Override
    public ResponseEntity<GroupsPageResponse> findGroups(Long size, Long groupId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> inviteGroup(Long groupId, Long memberId) {
        return null;
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
