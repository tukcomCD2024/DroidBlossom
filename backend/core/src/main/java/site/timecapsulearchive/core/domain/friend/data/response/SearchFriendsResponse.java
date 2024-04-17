package site.timecapsulearchive.core.domain.friend.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import site.timecapsulearchive.core.domain.friend.data.dto.PhoneBook;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;

@Schema(description = "전화번호 목록으로 찾은 회원 요약 정보 리스트 응답")
public record SearchFriendsResponse(

    @Schema(description = "회원 요약 정보")
    List<SearchFriendSummaryResponse> friends
) {

    public static SearchFriendsResponse createOf(
        final List<SearchFriendSummaryDto> dtos,
        final List<byte[]> phoneEncryption,
        final List<PhoneBook> phoneBooks
    ) {
        List<SearchFriendSummaryResponse> friends = dtos.stream()
            .flatMap(dto -> IntStream.range(0, phoneEncryption.size())
                .filter(index -> Arrays.equals(dto.phoneHash(), phoneEncryption.get(index)))
                .mapToObj(index -> dto.toResponse(phoneBooks.get(index))))
            .toList();

        return new SearchFriendsResponse(friends);
    }

}
