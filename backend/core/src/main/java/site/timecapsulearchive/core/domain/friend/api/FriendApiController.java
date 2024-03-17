package site.timecapsulearchive.core.domain.friend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.friend.data.reqeust.SearchFriendsRequest;
import site.timecapsulearchive.core.domain.friend.data.response.FriendReqStatusResponse;
import site.timecapsulearchive.core.domain.friend.data.response.FriendsPageResponse;
import site.timecapsulearchive.core.domain.friend.data.response.SearchFriendsResponse;
import site.timecapsulearchive.core.domain.friend.service.FriendService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendApiController implements FriendApi {

    private final FriendService friendService;

    @Override
    public ResponseEntity<Void> acceptFriendRequest(Long friendId) {
        return null;
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
        @AuthenticationPrincipal Long memberId,
        @PathVariable("friend_id") final Long friendId) {

        friendService.denyRequestFriend(memberId, friendId);

        return ResponseEntity.ok(ApiSpec.empty(SuccessCode.SUCCESS));

    }

    @Override
    public ResponseEntity<FriendsPageResponse> findFriends(Long friendId, Long size) {
        return null;
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

    @Override
    public ResponseEntity<SearchFriendsResponse> searchMembersByPhones(
        SearchFriendsRequest request) {
        return null;
    }
}
