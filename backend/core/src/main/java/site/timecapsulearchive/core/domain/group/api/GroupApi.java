package site.timecapsulearchive.core.domain.group.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.ZonedDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import site.timecapsulearchive.core.domain.group.data.reqeust.GroupCreateRequest;
import site.timecapsulearchive.core.domain.group.data.reqeust.GroupUpdateRequest;
import site.timecapsulearchive.core.domain.group.data.response.GroupDetailResponse;
import site.timecapsulearchive.core.domain.group.data.response.GroupsSliceResponse;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.error.ErrorResponse;

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
    ResponseEntity<ApiSpec<String>> createGroup(
        Long memberId,
        GroupCreateRequest request
    );

    @Operation(
        summary = "그룹 삭제",
        description = """
            그룹 삭제를 요청한 사용자가 해당 그룹의 그룹장인 경우 그룹을 삭제한다.<br>
            <b><u>주의</u></b>
            <ul>
                <li>그룹에 포함된 멤버가 그룹장을 제외하고 존재하면 그룹을 삭제할 수 없다.</li>
                <li>그룹장이 아닌 경우 그룹을 삭제할 수 없다.</li>
                <li>그룹에 그룹 캡슐이 남아있는 경우 삭제할 수 없다.</li>
            </ul>
            """,
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202",
            description = "처리 시작"
        ),
        @ApiResponse(
            responseCode = "400",
            description = """
                다음의 경우 예외가 발생한다.
                <ul>
                <li>삭제를 요청한 그룹에 그룹장을 제외한 그룹원이 존재하는 경우</li>
                <li>삭제를 요청한 그룹에서 요청한 사용자가 그룹장이 아닌 경우</li>
                <li>삭제를 요청한 그룹에 그룹 캡슐이 존재하는 경우</li>
                </ul>
                """,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "그룹이 존재하지 않으면 발생한다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
    })
    ResponseEntity<ApiSpec<String>> deleteGroupById(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "삭제할 그룹 아이디", required = true)
        Long groupId
    );

    @Operation(
        summary = "그룹원 삭제",
        description = "요청한 사용자가 그룹장인 경우 특정 그룹원을 그룹에서 삭제한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "그룹장과 삭제하려는 대상 그룹원 아이디가 같은 경우에 발생한다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "그룹장이 아니여서 그룹 수정에 대한 권한이 없는 경우 발생한다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "삭제하려는 그룹원이 존재하지 않는 경우 발생한다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<ApiSpec<String>> deleteGroupMember(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "그룹 아이디", required = true)
        Long groupId,

        @Parameter(in = ParameterIn.PATH, description = "삭제할 그룹원 멤버 아이디", required = true)
        Long groupMemberId
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
    @PostMapping(value = "/groups/{group_id}/members/{member_id}/invitation")
    ResponseEntity<Void> inviteGroup(
        @Parameter(in = ParameterIn.PATH, description = "그룹 아이디", required = true, schema = @Schema())
        @PathVariable("group_id") Long groupId,

        @Parameter(in = ParameterIn.PATH, description = "대상 회원 아이디", required = true, schema = @Schema())
        @PathVariable("member_id") Long memberId
    );

    @Operation(
        summary = "그룹 탈퇴",
        description = """
            그룹 탈퇴를 요청한 사용자가 해당 그룹의 그룹장이 아닌 경우 그룹을 탈퇴한다.<br>
            <b><u>주의</u></b>
            <ul>
                <li>그룹 탈퇴를 요청한 사용자가 해당 그룹의 그룹장인 경우 그룹을 탈퇴할 수 없다.</li>
            </ul>
            """,
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202",
            description = "처리 시작"
        ),
        @ApiResponse(
            responseCode = "400",
            description = """
                다음의 경우 예외가 발생한다.
                <ul>
                <li>탈퇴를 요청한 사용자가 그룹의 그룹장인 경우</li>
                </ul>
                """,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "그룹에 멤버가 존재하지 않으면 발생한다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<ApiSpec<String>> quitGroup(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "탈퇴할 그룹 아이디", required = true)
        Long groupId
    );

    @Operation(
        summary = "그룹 수정",
        description = "그룹장인 경우에 그룹의 기본 정보들을 수정한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        ),@ApiResponse(
            responseCode = "400",
            description = "잘못된 타입이나 값을 입력하는 경우 발생한다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "그룹장이 아니여서 그룹 수정에 대한 권한이 없는 경우 발생한다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "권한을 확인할 수 있는 그룹원이 없는 경우, 수정하려는 그룹이 없는 경우 발생한다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<ApiSpec<String>> updateGroupById(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "수정할 그룹 아이디", required = true, schema = @Schema())
        Long groupId,

        GroupUpdateRequest request
    );
}