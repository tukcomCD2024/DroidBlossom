package site.timecapsulearchive.core.domain.group.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.ZonedDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import site.timecapsulearchive.core.domain.group.data.reqeust.GroupCreateRequest;
import site.timecapsulearchive.core.domain.group.data.reqeust.GroupUpdateRequest;
import site.timecapsulearchive.core.domain.group.data.response.GroupDetailResponse;
import site.timecapsulearchive.core.domain.group.data.response.GroupsSliceResponse;
import site.timecapsulearchive.core.global.common.response.ApiSpec;

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
        summary = "그룹 생성",
        description = "친구로부터 그룹을 생성한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리완료"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "외부 API 요청 실패"
        )
    })
    ResponseEntity<ApiSpec<String>> createGroup(
        Long memberId,
        GroupCreateRequest request
    );

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
    ResponseEntity<ApiSpec<String>> rejectGroupInvitation(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "그룹 초대 대상 아이디", required = true)
        Long groupOwnerId
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
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 파라미터를 받았을 때 발생하는 오류"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "그룹에 포함된 사용자가 아닌 경우 발생하는 오류"
        )
    })
    ResponseEntity<ApiSpec<GroupDetailResponse>> findGroupDetailById(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "조회할 그룹 아이디", required = true, schema = @Schema())
        Long groupId
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
    ResponseEntity<ApiSpec<GroupsSliceResponse>> findGroups(
        Long memberId,

        @Parameter(in = ParameterIn.QUERY, description = "페이지 크기", required = true)
        int size,

        @Parameter(in = ParameterIn.QUERY, description = "마지막 데이터의 시간", required = true)
        ZonedDateTime createdAt
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
    ResponseEntity<ApiSpec<String>> inviteGroup(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "그룹 아이디", required = true)
        Long groupId,

        @Parameter(in = ParameterIn.PATH, description = "대상 회원 아이디", required = true)
        Long targetId
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