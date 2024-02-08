package site.timecapsulearchive.core.domain.member.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.member.data.reqeust.CheckStatusRequest;
import site.timecapsulearchive.core.domain.member.data.reqeust.MemberDetailUpdateRequest;
import site.timecapsulearchive.core.domain.member.data.response.MemberDetailResponse;
import site.timecapsulearchive.core.domain.member.data.response.MemberStatusResponse;
import site.timecapsulearchive.core.domain.member.service.MemberService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me")
public class MemberApiController implements MemberApi {

    private final MemberService memberService;

    @GetMapping(produces = {"application/json"})
    @Override
    public ResponseEntity<ApiSpec<MemberDetailResponse>> getMemberDetail(
        @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                memberService.findMemberDetailById(memberId)
            )
        );
    }

    @PostMapping(
        value = "/status",
        consumes = {"application/json"},
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<MemberStatusResponse>> checkStatus(
        @Valid @RequestBody final CheckStatusRequest request
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                memberService.checkStatus(
                    request.authId(),
                    request.socialType()
                )
            )
        );
    }

    @Override
    @PatchMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Void> updateMemberById(MemberDetailUpdateRequest request) {
        return null;
    }
}
