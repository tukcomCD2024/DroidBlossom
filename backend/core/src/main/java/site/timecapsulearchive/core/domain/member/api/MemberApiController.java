package site.timecapsulearchive.core.domain.member.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.auth.dto.request.CheckStatusRequest;
import site.timecapsulearchive.core.domain.member.dto.reqeust.MemberDetailUpdateRequest;
import site.timecapsulearchive.core.domain.member.dto.response.MemberDetailResponse;
import site.timecapsulearchive.core.domain.member.dto.response.MemberStatusResponse;
import site.timecapsulearchive.core.domain.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
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
    public ResponseEntity<MemberStatusResponse> checkStatus(
        @Valid @RequestBody CheckStatusRequest request
    ) {
        return ResponseEntity.ok(memberService.checkStatus(
            request.authId(),
            request.socialType())
        );
    }
}
