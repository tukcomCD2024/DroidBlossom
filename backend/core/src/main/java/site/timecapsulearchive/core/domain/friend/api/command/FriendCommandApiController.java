package site.timecapsulearchive.core.domain.friend.api.command;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.friend.data.request.SendFriendRequest;
import site.timecapsulearchive.core.domain.friend.service.command.FriendCommandService;
import site.timecapsulearchive.core.domain.member.service.MemberService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendCommandApiController implements FriendCommandApi {

    private final FriendCommandService friendCommandService;
    private final MemberService memberService;

    @DeleteMapping(value = "/{friend_id}")
    @Override
    public ResponseEntity<ApiSpec<String>> deleteFriend(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("friend_id") final Long friendId
    ) {
        friendCommandService.deleteFriend(memberId, friendId);

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.SUCCESS
            )
        );
    }

    @DeleteMapping("/{friend_id}/sending-invites")
    @Override
    public ResponseEntity<ApiSpec<String>> deleteSendingFriendInvite(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("friend_id") final Long friendId
    ) {
        friendCommandService.deleteSendingFriendInvite(memberId, friendId);

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.SUCCESS
            )
        );
    }


    @DeleteMapping("/{friend_id}/deny-request")
    @Override
    public ResponseEntity<ApiSpec<String>> denyFriendRequest(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("friend_id") final Long friendId) {

        friendCommandService.denyRequestFriend(memberId, friendId);

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.SUCCESS
            )
        );
    }

    @PostMapping(value = "/{friend_id}/request")
    @Override
    public ResponseEntity<ApiSpec<String>> requestFriend(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("friend_id") final Long friendId) {

        friendCommandService.requestFriend(memberId, friendId);

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.ACCEPTED
            )
        );
    }

    @PostMapping(value = "/requests")
    @Override
    public ResponseEntity<ApiSpec<String>> requestFriends(
        @AuthenticationPrincipal final Long memberId,
        @RequestBody SendFriendRequest request
    ) {
        friendCommandService.requestFriends(memberId, request.friendIds());

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.ACCEPTED
            )
        );
    }

    @PostMapping(value = "/{friend_id}/accept-request")
    @Override
    public ResponseEntity<ApiSpec<String>> acceptFriendRequest(
        @AuthenticationPrincipal final Long memberId,
        @PathVariable("friend_id") final Long friendId
    ) {
        friendCommandService.acceptFriend(memberId, friendId);

        return ResponseEntity.ok(
            ApiSpec.empty(
                SuccessCode.SUCCESS
            )
        );
    }
}
