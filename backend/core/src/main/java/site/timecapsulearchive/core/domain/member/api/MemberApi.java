package site.timecapsulearchive.core.domain.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import site.timecapsulearchive.core.domain.member.dto.reqeust.CheckStatusRequest;
import site.timecapsulearchive.core.domain.member.dto.reqeust.MemberDetailUpdateRequest;
import site.timecapsulearchive.core.domain.member.dto.response.MemberDetailResponse;
import site.timecapsulearchive.core.domain.member.dto.response.MemberStatusResponse;
import site.timecapsulearchive.core.global.common.response.ApiSpec;

public interface MemberApi {

    @Operation(
        summary = "회원 상세 정보 조회",
        description = "회원의 상세 정보를 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"member"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        )
    })
    @GetMapping(
        value = "/",
        produces = {"application/json"}
    )
    ResponseEntity<MemberDetailResponse> findMemberById();

    @Operation(
        summary = "회원 수정",
        description = "회원의 상세 정보를 수정한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"member"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        )
    })
    @PatchMapping(
        value = "/",
        consumes = {"multipart/form-data"}
    )
    ResponseEntity<Void> updateMemberById(@ModelAttribute MemberDetailUpdateRequest request);

    @Operation(
        summary = "다른 OAuth2 프로바이더의 아이디로 앱 내의 유저의 인증 상태를 반환",
        description = "Google, Kakao 프로바이더가 제공하는 유저 아이디로 앱 내의 인증 상태를 반환한다.",
        tags = {"member"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    @PostMapping(
        value = "/status",
        consumes = {"application/json"},
        produces = {"application/json"}
    )
    ResponseEntity<ApiSpec<MemberStatusResponse>> checkStatus(
        CheckStatusRequest request);
}
