package site.timecapsulearchive.core.domain.member.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.member.dto.reqeust.CheckStatusRequest;
import site.timecapsulearchive.core.domain.member.dto.reqeust.MemberDetailUpdateRequest;
import site.timecapsulearchive.core.domain.member.dto.response.MemberDetailResponse;
import site.timecapsulearchive.core.domain.member.dto.response.MemberStatusResponse;
import site.timecapsulearchive.core.domain.member.service.MemberService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me")
public class MemberApiController implements MemberApi {

    private final MemberService memberService;

    @Override
    public ResponseEntity<MemberDetailResponse> findMemberById() {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateMemberById(MemberDetailUpdateRequest request) {
        return null;
    }

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
}
