package site.timecapsulearchive.core.domain.group_member.api.command;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import site.timecapsulearchive.core.global.common.response.ApiSpec;

public interface GroupMemberCommandApi {

    @Operation(
        summary = "그룹원 삭제",
        description = "그룹장인 경우 특정 그룹원을 삭제한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group member"}
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
        summary = "그룹 탈퇴",
        description = "사용자가 속한 그룹을 탈퇴한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group member"}
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
        summary = "그룹 요청",
        description = "그룹장인 경우 친구에게 그룹 가입 요청을 할 수 있다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group member"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202",
            description = "처리 시작"
        )
    })
    ResponseEntity<ApiSpec<String>> inviteGroup(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "그룹 아이디", required = true)
        Long groupId,

        @Parameter(in = ParameterIn.PATH, description = "대상 회원 아이디", required = true)
        Long targetId
    );

    @Operation(
        summary = "그룹 요청 수락",
        description = "특정 그룹으로부터 그룹 요청을 수락한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group member"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "그룹 초대 찾기 실패"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "외부 API 요청 실패"
        )
    })
    ResponseEntity<ApiSpec<String>> acceptGroupInvitation(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "그룹 아이디", required = true)
        Long groupId,

        @Parameter(in = ParameterIn.PATH, description = "대상 회원 아이디", required = true)
        Long targetId
    );

    @Operation(
        summary = "그룹 요청 거부",
        description = "특정 그룹으로부터 초대 요청을 거부한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group member"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        )
    })
    ResponseEntity<ApiSpec<String>> rejectGroupInvitation(
        Long memberId,

        Long groupId,

        @Parameter(in = ParameterIn.PATH, description = "그룹 초대 대상 아이디", required = true)
        Long groupOwnerId
    );
}
