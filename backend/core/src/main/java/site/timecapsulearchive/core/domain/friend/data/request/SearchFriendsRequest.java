package site.timecapsulearchive.core.domain.friend.data.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.UnaryOperator;
import site.timecapsulearchive.core.domain.friend.data.dto.PhoneBook;
import site.timecapsulearchive.core.global.common.wrapper.ByteArrayWrapper;

@Schema(description = "전화번호 리스트로 친구 찾기 요청")
public record SearchFriendsRequest(

    @Schema(description = "전화번호부")
    List<PhoneBook> phoneBooks
) {

    public List<ByteArrayWrapper> toPhoneEncryption(
        final UnaryOperator<byte[]> hashEncryptionFunction
    ) {
        return phoneBooks.stream()
            .map(phoneBook -> new ByteArrayWrapper(hashEncryptionFunction.apply(
                phoneBook.originPhone().getBytes(StandardCharsets.UTF_8))))
            .toList();
    }
}
