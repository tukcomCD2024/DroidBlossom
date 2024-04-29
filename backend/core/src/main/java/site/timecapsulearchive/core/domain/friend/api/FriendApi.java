package site.timecapsulearchive.core.domain.friend.api;

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
import site.timecapsulearchive.core.domain.friend.data.request.SearchFriendsRequest;
import site.timecapsulearchive.core.domain.friend.data.request.SendFriendRequest;
import site.timecapsulearchive.core.domain.friend.data.response.FriendReqStatusResponse;
import site.timecapsulearchive.core.domain.friend.data.response.FriendRequestsSliceResponse;
import site.timecapsulearchive.core.domain.friend.data.response.FriendsSliceResponse;
import site.timecapsulearchive.core.domain.friend.data.response.SearchFriendsResponse;
import site.timecapsulearchive.core.domain.friend.data.response.SearchTagFriendSummaryResponse;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.error.ErrorResponse;

public interface FriendApi {

    @Operation(
        summary = "소셜 친구 요청 수락",
        description = "상대방으로부터 온 친구 요청을 수락한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"friend"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "친구 요청이 존재하지 않는 경우 발생"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "외부 API 요청 실패"
        )
    })
    ResponseEntity<ApiSpec<String>> acceptFriendRequest(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, required = true, schema = @Schema())
        Long friendId
    );


    @Operation(
        summary = "소셜 친구 삭제",
        description = "사용자의 소셜 친구를 삭제한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"friend"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        ),
        @ApiResponse(
            responseCode = "400",
            description = """
                잘못된 요청 파라미터 또는 존재하지 않는 사용자로 요청하거나 존재하지 않는 친구에게 친구 요청을 보낼 경우 발생
                """,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<ApiSpec<String>> deleteFriend(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, required = true, schema = @Schema())
        Long friendId
    );


    @Operation(
        summary = "소셜 친구 요청 거부",
        description = "상대방으로부터 온 친구 요청을 거부한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"friend"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        )
    })
    ResponseEntity<ApiSpec<String>> denyFriendRequest(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, required = true, schema = @Schema())
        Long friendId
    );

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
        summary = "소셜 친구 요청",
        description = "사용자에게 친구 요청을 보낸다. 해당 사용자에게 알림이 발송된다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"friend"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202",
            description = "처리 시작"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "외부 API 요청 실패"
        )
    })
    ResponseEntity<ApiSpec<FriendReqStatusResponse>> requestFriend(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "친구 아이디", required = true, schema = @Schema())
        Long friendId
    );

    @Operation(
        summary = "소셜 친구들에게 친구 요청",
        description = "사용자들에게 친구 요청을 보낸다. 해당 사용자들에게 알림이 발송된다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"friend"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202",
            description = "처리 시작"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "외부 API 요청 실패"
        )
    })
    ResponseEntity<ApiSpec<String>> requestFriends(
        Long memberId,

        SendFriendRequest request
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
        summary = "찬구 검색",
        description = "친구의 tag로 친구 검색을 한다.",
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
