package site.timecapsulearchive.core.domain.group.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.timecapsulearchive.core.domain.group.data.reqeust.GroupCreateRequest;
import site.timecapsulearchive.core.domain.group.data.reqeust.GroupUpdateRequest;
import site.timecapsulearchive.core.domain.group.data.response.GroupDetailResponse;
import site.timecapsulearchive.core.domain.group.data.response.GroupSummaryResponse;
import site.timecapsulearchive.core.domain.group.data.response.GroupsPageResponse;

public interface GroupApi {

    @Operation(
        summary = "그룹 요청 수락",
        description = "특정 그룹으로부터 그룹 요청을 수락한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        )
    })
    @PostMapping(value = "/groups/{group_id}/members/{member_id}/accept-invitation")
    ResponseEntity<Void> acceptGroupInvitation(
        @Parameter(in = ParameterIn.PATH, description = "그룹 아이디", required = true, schema = @Schema())
        @PathVariable("group_id") Long groupId,

        @Parameter(in = ParameterIn.PATH, description = "대상 회원 아이디", required = true, schema = @Schema())
        @PathVariable("member_id") Long memberId
    );

    @Operation(
        summary = "그룹 생성",
        description = "친구로부터 그룹을 생성한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리완료"
        )
    })
    @PostMapping(
        value = "/groups",
        consumes = {"multipart/form-data"}
    )
    ResponseEntity<GroupSummaryResponse> createGroup(@ModelAttribute GroupCreateRequest request);

    @Operation(
        summary = "그룹 삭제",
        description = "그룹장인 경우에 특정 그룹을 삭제한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "처리완료"
        )
    })
    @DeleteMapping(value = "/groups/{group_id}")
    ResponseEntity<Void> deleteGroupById(
        @Parameter(in = ParameterIn.PATH, description = "수정할 그룹 아이디", required = true, schema = @Schema())
        @PathVariable("group_id") Long groupId
    );

    @Operation(
        summary = "그룹원 삭제",
        description = "그룹장인 경우 특정 그룹원을 삭제한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "처리 완료"
        )
    })
    @DeleteMapping(value = "/groups/{group_id}/members/{member_id}")
    ResponseEntity<Void> deleteGroupMember(
        @Parameter(in = ParameterIn.PATH, description = "그룹 아이디", required = true, schema = @Schema())
        @PathVariable("group_id") Long groupId,

        @Parameter(in = ParameterIn.PATH, description = "삭제할 멤버 아이디", required = true, schema = @Schema())
        @PathVariable("member_id") Long memberId
    );

    @Operation(
        summary = "그룹 요청 거부",
        description = "특정 그룹으로부터 초대 요청을 거부한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        )
    })
    @PostMapping(value = "/groups/{group_id}/members/{member_id}/deny-invitation")
    ResponseEntity<Void> denyGroupInvitation(
        @Parameter(in = ParameterIn.PATH, description = "그룹 아이디", required = true, schema = @Schema())
        @PathVariable("group_id") Long groupId,

        @Parameter(in = ParameterIn.PATH, description = "대상 회원 아이디", required = true, schema = @Schema())
        @PathVariable("member_id") Long memberId
    );

    @Operation(
        summary = "그룹 상세 조회",
        description = "그룹의 상세 정보를 보여준다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    @GetMapping(
        value = "/groups/{group_id}",
        produces = {"application/json"}
    )
    ResponseEntity<GroupDetailResponse> findGroupById(
        @Parameter(in = ParameterIn.PATH, description = "조회할 그룹 아이디", required = true, schema = @Schema())
        @PathVariable("group_id") Long groupId
    );

    @Operation(
        summary = "그룹 목록 조회",
        description = "사용자가 소속된 그룹의 목록을 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    @GetMapping(
        value = "/groups",
        produces = {"application/json"}
    )
    ResponseEntity<GroupsPageResponse> findGroups(
        @Parameter(in = ParameterIn.QUERY, description = "페이지 크기", required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "size") Long size,

        @Parameter(in = ParameterIn.QUERY, description = "마지막 그룹 아이디", required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "group_id") Long groupId
    );

    @Operation(
        summary = "그룹 요청",
        description = "그룹장인 경우 친구에게 그룹 가입 요청을 할 수 있다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202",
            description = "처리 시작"
        )
    })
    @PostMapping(value = "/groups/{group_id}/members/{member_id}/invitation")
    ResponseEntity<Void> inviteGroup(
        @Parameter(in = ParameterIn.PATH, description = "그룹 아이디", required = true, schema = @Schema())
        @PathVariable("group_id") Long groupId,

        @Parameter(in = ParameterIn.PATH, description = "대상 회원 아이디", required = true, schema = @Schema())
        @PathVariable("member_id") Long memberId
    );

    @Operation(
        summary = "그룹 탈퇴",
        description = "사용자가 속한 그룹을 탈퇴한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "처리 완료"
        )
    })
    @DeleteMapping(value = "/groups/{group_id}/members/quit")
    ResponseEntity<Void> quitGroup(
        @Parameter(in = ParameterIn.PATH, description = "조회할 그룹 아이디", required = true, schema = @Schema())
        @PathVariable("group_id") Long groupId
    );

    @Operation(
        summary = "그룹 수정",
        description = "그룹장인 경우에 그룹의 기본 정보들을 수정한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202",
            description = "처리 시작"
        )
    })
    @PatchMapping(
        value = "/groups/{group_id}",
        consumes = {"multipart/form-data"}
    )
    ResponseEntity<Void> updateGroupById(
        @Parameter(in = ParameterIn.PATH, description = "수정할 그룹 아이디", required = true, schema = @Schema())
        @PathVariable("group_id") Long groupId,

        @ModelAttribute GroupUpdateRequest request
    );
}