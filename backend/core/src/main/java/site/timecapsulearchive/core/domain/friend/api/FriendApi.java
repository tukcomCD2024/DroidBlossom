package site.timecapsulearchive.core.domain.friend.api;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import site.timecapsulearchive.core.domain.friend.dto.reqeust.SearchFriendsRequest;
import site.timecapsulearchive.core.domain.friend.dto.response.FriendsPageResponse;
import site.timecapsulearchive.core.domain.friend.dto.response.SearchFriendsResponse;

public interface FriendApi {

    @Operation(
        summary = "친구 요청 수락",
        description = "상대방으로부터 온 친구 요청을 수락한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"friend"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        )
    })
    @PostMapping(value = "/friends/accept-request")
    ResponseEntity<Void> acceptFriendRequest(
        @Parameter(in = ParameterIn.QUERY, required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "friend_id") Long friendId
    );


    @Operation(
        summary = "친구 삭제",
        description = "사용자의 소셜 친구를 삭제한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"friend"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "처리 완료"
        )
    })
    @DeleteMapping(value = "/friends/{friend_id}")
    ResponseEntity<Void> deleteFriend(
        @Parameter(in = ParameterIn.PATH, required = true, schema = @Schema())
        @PathVariable("friend_id") Long friendId
    );


    @Operation(
        summary = "친구 요청 거부",
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
    @PostMapping(value = "/friends/deny-request")
    ResponseEntity<Void> denyFriendRequest(
        @Parameter(in = ParameterIn.QUERY, required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "friend_id") Long friendId
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
    @GetMapping(
        value = "/friends",
        produces = {"application/json"}
    )
    ResponseEntity<FriendsPageResponse> findFriends(
        @Parameter(in = ParameterIn.QUERY, description = "마지막 친구 아이디", required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "friend_id") Long friendId,

        @Parameter(in = ParameterIn.QUERY, description = "페이지 크기", schema = @Schema())
        @NotNull @Valid @RequestParam(value = "size", required = false) Long size
    );


    @Operation(
        summary = "친구 요청",
        description = "사용자에게 친구 요청을 보낸다. 해당 사용자에게 알림이 발송된다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"friend"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202",
            description = "처리 시작"
        )
    })
    @PostMapping(value = "/friends/request")
    ResponseEntity<Void> requestFriend(
        @Parameter(in = ParameterIn.QUERY, required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "friend_id") Long friendId
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
    @PostMapping(
        value = "/friends/search",
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    ResponseEntity<SearchFriendsResponse> searchMembersByPhones(
        @RequestBody SearchFriendsRequest request);
}
