package site.timecapsulearchive.core.domain.group.api.command;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.group.data.dto.GroupCreateDto;
import site.timecapsulearchive.core.domain.group.data.reqeust.GroupCreateRequest;
import site.timecapsulearchive.core.domain.group.data.reqeust.GroupUpdateRequest;
import site.timecapsulearchive.core.domain.group.service.command.GroupCommandService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;
import site.timecapsulearchive.core.infra.s3.manager.S3UrlGenerator;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupCommandApiController implements GroupCommandApi {

    private final GroupCommandService groupCommandService;

    @Override
    @PostMapping
    public ResponseEntity<ApiSpec<String>> createGroup(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody final GroupCreateRequest request
    ) {
        final String groupProfileUrl = S3UrlGenerator.generateFileName(memberId,
            request.groupDirectory(), request.groupImage());
        final GroupCreateDto dto = request.toDto(groupProfileUrl);

        groupCommandService.createGroup(memberId, dto);

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.ACCEPTED
            )
        );
    }

    @DeleteMapping(value = "/{group_id}")
    @Override
    public ResponseEntity<ApiSpec<String>> deleteGroupById(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("group_id") final Long groupId
    ) {
        groupCommandService.deleteGroup(memberId, groupId);

        return ResponseEntity.ok(ApiSpec.empty(SuccessCode.SUCCESS));
    }

    @PatchMapping(value = "/{group_id}", consumes = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<String>> updateGroupById(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("group_id") final Long groupId,
        @Valid @RequestBody final GroupUpdateRequest request
    ) {
        groupCommandService.updateGroup(memberId, groupId, request.toDto());

        return ResponseEntity.ok(ApiSpec.empty(SuccessCode.SUCCESS));
    }
}
