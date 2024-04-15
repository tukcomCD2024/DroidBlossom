package site.timecapsulearchive.core.domain.friend.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.Function;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.request.SearchFriendsRequest;

@Schema(description = "전화번호 목록으로 찾은 회원 요약 정보 리스트 응답")
public record SearchFriendsResponse(

    @Schema(description = "회원 요약 정보")
    List<SearchFriendSummaryResponse> friends
) {

    public static SearchFriendsResponse createOf(
        final List<SearchFriendSummaryDto> dtos,
        final SearchFriendsRequest request,
        final Function<byte[], String> aesEncryptionFunction
    ) {
        final List<SearchFriendSummaryResponse> friends = dtos.stream()
            .map(dto -> dto.toResponse(request, aesEncryptionFunction))
            .toList();

        return new SearchFriendsResponse(friends);
    }

}
