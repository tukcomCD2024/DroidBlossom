package site.timecapsulearchive.core.domain.friend.api.command;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import site.timecapsulearchive.core.domain.friend.data.request.SendFriendRequest;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.error.ErrorResponse;

public interface FriendCommandApi {

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
        summary = "소셜 친구 요청 삭제",
        description = "사용자가 보낸 소셜 친구 요청을 삭제한다.",
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
    ResponseEntity<ApiSpec<String>> deleteSendingFriendInvite(
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
    ResponseEntity<ApiSpec<String>> requestFriend(
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
}
