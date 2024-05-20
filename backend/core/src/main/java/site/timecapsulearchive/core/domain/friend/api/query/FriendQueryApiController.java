package site.timecapsulearchive.core.domain.friend.api.query;

import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.request.SearchFriendsRequest;
import site.timecapsulearchive.core.domain.friend.data.response.FriendRequestsSliceResponse;
import site.timecapsulearchive.core.domain.friend.data.response.FriendsSliceResponse;
import site.timecapsulearchive.core.domain.friend.data.response.SearchFriendsResponse;
import site.timecapsulearchive.core.domain.friend.data.response.SearchTagFriendSummaryResponse;
import site.timecapsulearchive.core.domain.friend.service.query.FriendQueryService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;
import site.timecapsulearchive.core.global.common.wrapper.ByteArrayWrapper;
import site.timecapsulearchive.core.global.security.encryption.HashEncryptionManager;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendQueryApiController implements FriendQueryApi {

    private final FriendQueryService friendQueryService;
    private final HashEncryptionManager hashEncryptionManager;

    @GetMapping
    @Override
    public ResponseEntity<ApiSpec<FriendsSliceResponse>> findFriends(
        @AuthenticationPrincipal final Long memberId,
        @RequestParam(defaultValue = "20", value = "size") final int size,
        @RequestParam(value = "created_at") final ZonedDateTime createdAt
    ) {
        Slice<FriendSummaryDto> friendsSlice = friendQueryService.findFriendsSlice(memberId, size,
            createdAt);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                FriendsSliceResponse.createOf(friendsSlice.getContent(), friendsSlice.hasNext())
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
        @RequestParam(value = "created_at") final ZonedDateTime createdAt
    ) {
        Slice<FriendSummaryDto> friendRequestsSlice = friendQueryService.findFriendRequestsSlice(
            memberId, size, createdAt);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                FriendRequestsSliceResponse.createOf(friendRequestsSlice.getContent(),
                    friendRequestsSlice.hasNext())
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
        final List<ByteArrayWrapper> phoneEncryption = request.toPhoneEncryption(
            hashEncryptionManager::encrypt);

        final List<SearchFriendSummaryDto> dtos = friendQueryService.findFriendsByPhone(
            memberId, phoneEncryption);

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                SearchFriendsResponse.createOf(dtos, phoneEncryption, request.phoneBooks())
            )
        );
    }

    @GetMapping(value = "/search")
    @Override
    public ResponseEntity<ApiSpec<SearchTagFriendSummaryResponse>> searchFriendByTag(
        @AuthenticationPrincipal Long memberId,
        @RequestParam(value = "friend_tag") final String tag
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                friendQueryService.searchFriend(memberId, tag)
            )
        );
    }
}
