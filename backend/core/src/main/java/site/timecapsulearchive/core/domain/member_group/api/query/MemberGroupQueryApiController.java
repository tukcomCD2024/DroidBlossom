package site.timecapsulearchive.core.domain.member_group.api.query;

import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupInviteSummaryDto;
import site.timecapsulearchive.core.domain.member_group.data.response.GroupInviteSummaryResponses;
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
        value = "/invites",
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<GroupInviteSummaryResponses>> findGroupInvites(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(defaultValue = "20", value = "size") final int size,
        @RequestParam(value = "created_at") final ZonedDateTime createdAt
    ) {
        final Slice<GroupInviteSummaryDto> groupInviteSummaryDtos = memberGroupQueryService.findGroupInvites(
            memberId, size, createdAt);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                GroupInviteSummaryResponses.createOf(
                    groupInviteSummaryDtos.getContent(),
                    groupInviteSummaryDtos.hasNext(),
                    s3PreSignedUrlManager::getS3PreSignedUrlForGet
                )
            )
        );
    }

}