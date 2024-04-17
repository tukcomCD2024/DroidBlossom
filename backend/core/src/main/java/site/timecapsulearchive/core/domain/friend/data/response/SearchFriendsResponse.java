package site.timecapsulearchive.core.domain.friend.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import site.timecapsulearchive.core.domain.friend.data.dto.PhoneBook;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;
import site.timecapsulearchive.core.global.common.wrapper.ByteArrayWrapper;

@Schema(description = "전화번호 목록으로 찾은 회원 요약 정보 리스트 응답")
public record SearchFriendsResponse(

    @Schema(description = "회원 요약 정보")
    List<SearchFriendSummaryResponse> friends
) {

    public static SearchFriendsResponse createOf(
        final List<SearchFriendSummaryDto> dtos,
        final List<ByteArrayWrapper> phoneEncryption,
        final List<PhoneBook> phoneBooks
    ) {
        List<SearchFriendSummaryResponse> friends = dtos.stream()
            .filter(dto -> phoneEncryption.contains(dto.phoneHash()))
            .map(dto -> {
                int index = phoneEncryption.indexOf(dto.phoneHash());
                return dto.toResponse(phoneBooks.get(index));
            })
            .toList();

        return new SearchFriendsResponse(friends);
    }

}
