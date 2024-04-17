package site.timecapsulearchive.core.domain.friend.data.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
import site.timecapsulearchive.core.domain.friend.data.dto.PhoneBook;

@Schema(description = "전화번호 리스트로 친구 찾기 요청")
public record SearchFriendsRequest(

    @Schema(description = "전화번호부")
    List<PhoneBook> phoneBooks
) {

    public List<byte[]> toPoneEncryption(
        final Function<byte[], byte[]> hashEncryptionFunction
    ) {
        return phoneBooks.stream()
            .map(phoneBook -> hashEncryptionFunction.apply(
                phoneBook.originPhone().getBytes(StandardCharsets.UTF_8)))
            .toList();
    }
}
