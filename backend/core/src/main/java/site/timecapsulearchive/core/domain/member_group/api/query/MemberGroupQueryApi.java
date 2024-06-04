package site.timecapsulearchive.core.domain.member_group.api.query;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.ZonedDateTime;
import org.springframework.http.ResponseEntity;
import site.timecapsulearchive.core.domain.group.data.response.GroupMemberInfosResponse;
import site.timecapsulearchive.core.domain.member_group.data.response.GroupReceivingInvitesSliceResponse;
import site.timecapsulearchive.core.domain.member_group.data.response.GroupSendingInvitesResponse;
import site.timecapsulearchive.core.global.common.response.ApiSpec;

public interface MemberGroupQueryApi {


    @Operation(
        summary = "그룹 요청 받은 목록 조회",
        description = "사용자에게 그룹 초대 요청이 온 모든 그룹 목록을 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"member group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    ResponseEntity<ApiSpec<GroupReceivingInvitesSliceResponse>> findGroupReceivingInvites(
        Long memberId,

        @Parameter(in = ParameterIn.QUERY, description = "페이지 크기", required = true)
        int size,

        @Parameter(in = ParameterIn.QUERY, description = "마지막 데이터의 시간", required = true)
        ZonedDateTime createdAt
    );

    @Operation(
        summary = "그룹에 대한 그룹 멤버 조회",
        description = "그룹의 그룹원들 정보를 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    ResponseEntity<ApiSpec<GroupMemberInfosResponse>> findGroupMemberInfos(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "조회할 그룹 아이디", required = true)
        Long groupId
    );

    @Operation(
        summary = "그룹 요청 보낸 목록 조회",
        description = "그룹장이 그룹 별로 그룹 초대 요청을 보낸 사용자 목록을 조회한다. 최대 30개가 반환된다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"member group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    ResponseEntity<ApiSpec<GroupSendingInvitesResponse>> findGroupSendingInvites(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "그룹 아이디", required = true)
        Long groupId
    );
}
