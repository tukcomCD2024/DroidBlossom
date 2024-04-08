package site.timecapsulearchive.core.domain.friend.api;

import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.friend.data.reqeust.SearchFriendsRequest;
import site.timecapsulearchive.core.domain.friend.data.response.FriendReqStatusResponse;
import site.timecapsulearchive.core.domain.friend.data.response.FriendRequestsSliceResponse;
import site.timecapsulearchive.core.domain.friend.data.response.FriendsSliceResponse;
import site.timecapsulearchive.core.domain.friend.data.response.SearchFriendsResponse;
import site.timecapsulearchive.core.domain.friend.data.response.SearchTagFriendSummaryResponse;
import site.timecapsulearchive.core.domain.friend.service.FriendService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendApiController implements FriendApi {

    private final FriendService friendService;

    @PostMapping(value = "/{friend_id}/accept-request")
    @Override
    public ResponseEntity<ApiSpec<String>> acceptFriendRequest(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("friend_id") final Long friendId
    ) {
        friendService.acceptFriend(memberId, friendId);

        return ResponseEntity.ok(ApiSpec.empty(SuccessCode.SUCCESS));
    }

    @GetMapping
    @Override
    public ResponseEntity<ApiSpec<FriendsSliceResponse>> findFriends(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(defaultValue = "20", value = "size") final int size,
        @RequestParam(value = "createdAt") final ZonedDateTime createdAt
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                friendService.findFriendsSlice(memberId, size, createdAt)
            )
        );
    }

    @GetMapping(
        value = "/requests",
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<FriendRequestsSliceResponse>> findFriendRequests(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(defaultValue = "20", value = "size") final int size,
        @RequestParam(value = "createdAt") final ZonedDateTime createdAt
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                friendService.findFriendRequestsSlice(memberId, size, createdAt)
            )
        );
    }

    @DeleteMapping(value = "/{friend_id}")
    @Override
    public ResponseEntity<ApiSpec<String>> deleteFriend(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("friend_id") final Long friendId
    ) {
        friendService.deleteFriend(memberId, friendId);

        return ResponseEntity.ok(
            ApiSpec.empty(SuccessCode.SUCCESS)
        );
    }

    @DeleteMapping("{friend_id}/deny-request")
    @Override
    public ResponseEntity<ApiSpec<String>> denyFriendRequest(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("friend_id") final Long friendId) {

        friendService.denyRequestFriend(memberId, friendId);

        return ResponseEntity.ok(ApiSpec.empty(SuccessCode.SUCCESS));

    }

    @PostMapping(value = "/{friend_id}/request")
    @Override
    public ResponseEntity<ApiSpec<FriendReqStatusResponse>> requestFriend(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("friend_id") final Long friendId) {

        return ResponseEntity.accepted()
            .body(
                ApiSpec.success(
                    SuccessCode.SUCCESS,
                    friendService.requestFriend(memberId, friendId)
                )
            );
    }

    @PostMapping(
        value = "/search/phone",
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<SearchFriendsResponse>> searchMembersByPhones(
        @AuthenticationPrincipal Long memberId,
        @Valid @RequestBody SearchFriendsRequest request
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                friendService.findFriendsByPhone(memberId, request.phones())
            )
        );
    }

    @GetMapping(value = "/search")
    @Override
    public ResponseEntity<ApiSpec<SearchTagFriendSummaryResponse>> searchFriendByTag(
        @AuthenticationPrincipal Long memberId,
        @RequestParam(value = "friend-tag") final String tag
    ) {
        return ResponseEntity.ok()
            .body(
                ApiSpec.success(
                    SuccessCode.SUCCESS,
                    friendService.searchFriend(memberId, tag)
                )
            );
    }
}
