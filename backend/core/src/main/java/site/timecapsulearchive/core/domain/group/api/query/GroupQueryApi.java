package site.timecapsulearchive.core.domain.group.api.query;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.ZonedDateTime;
import org.springframework.http.ResponseEntity;
import site.timecapsulearchive.core.domain.group.data.response.GroupDetailResponse;
import site.timecapsulearchive.core.domain.group.data.response.GroupMemberInfosResponse;
import site.timecapsulearchive.core.domain.group.data.response.GroupsSliceResponse;
import site.timecapsulearchive.core.global.common.response.ApiSpec;

public interface GroupQueryApi {

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

        @Parameter(in = ParameterIn.PATH, description = "조회할 그룹 아이디", required = true)
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
}
