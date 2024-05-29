package site.timecapsulearchive.core.domain.friend.api.query;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.ZonedDateTime;
import org.springframework.http.ResponseEntity;
import site.timecapsulearchive.core.domain.friend.data.request.SearchFriendsRequest;
import site.timecapsulearchive.core.domain.friend.data.response.FriendRequestsSliceResponse;
import site.timecapsulearchive.core.domain.friend.data.response.FriendsSliceResponse;
import site.timecapsulearchive.core.domain.friend.data.response.SearchFriendsResponse;
import site.timecapsulearchive.core.domain.friend.data.response.SearchTagFriendSummaryResponse;
import site.timecapsulearchive.core.global.common.response.ApiSpec;

public interface FriendQueryApi {

    @Operation(
        summary = "소셜 친구 목록 조회",
        description = "사용자의 소셜 친구 목록을 보여준다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"friend"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    ResponseEntity<ApiSpec<FriendsSliceResponse>> findFriends(
        Long memberId,

        @Parameter(in = ParameterIn.QUERY, description = "페이지 크기", required = true)
        int size,

        @Parameter(in = ParameterIn.QUERY, description = "마지막 데이터의 시간", required = true)
        ZonedDateTime createdAt
    );

    @Operation(
        summary = "그룹 초대 전 친구 목록 조회",
        description = "그룹에 초대할 수 있는 사용자의 소셜 친구 목록을 보여준다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"friend"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    ResponseEntity<ApiSpec<FriendsSliceResponse>> findFriendsBeforeGroupInvite(
        Long memberId,

        @Parameter(in = ParameterIn.QUERY, description = "초대할 그룹 ID", required = true)
        Long groupId,

        @Parameter(in = ParameterIn.QUERY, description = "페이지 크기", required = true)
        int size,

        @Parameter(in = ParameterIn.QUERY, description = "마지막 데이터의 시간", required = true)
        ZonedDateTime createdAt
    );

    @Operation(
        summary = "소셜 친구 요청 목록 조회",
        description = "사용자의 소셜 친구 요청 목록을 보여준다. 수락 대기 중인 요청만 해당한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"friend"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    ResponseEntity<ApiSpec<FriendRequestsSliceResponse>> findFriendRequests(
        Long memberId,

        @Parameter(in = ParameterIn.QUERY, description = "페이지 크기", required = true)
        int size,

        @Parameter(in = ParameterIn.QUERY, description = "마지막 데이터의 시간", required = true)
        ZonedDateTime createdAt
    );

    @Operation(
        summary = "전화번호를 바탕으로 앱 사용자 조회",
        description = "전화번호 목록을 받아 ARchive 사용자를 반환한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"friend"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    ResponseEntity<ApiSpec<SearchFriendsResponse>> searchMembersByPhones(
        Long memberId,
        SearchFriendsRequest request
    );

    @Operation(
        summary = "친구 검색",
        description = """
        친구의 tag로 친구 검색을 한다.
        <br>
        태그가 일치하면 일치하는 태그를 가진 사용자를 일치하지 않으면 가장 비슷한 태그를 가진 사용자를 반환한다.
        """,
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"friend"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "ok"
        )
    })
    ResponseEntity<ApiSpec<SearchTagFriendSummaryResponse>> searchFriendByTag(
        Long memberId,

        @Parameter(in = ParameterIn.QUERY, description = "친구 태그", required = true)
        String tag
    );

}
