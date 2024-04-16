package site.timecapsulearchive.core.domain.friend.data.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import site.timecapsulearchive.core.domain.friend.data.dto.PhoneBook;
import site.timecapsulearchive.core.global.common.valid.annotation.Phone;

@Schema(description = "전화번호 리스트로 친구 찾기 요청")
public record SearchFriendsRequest(

    @Schema(description = "전화번호부")
    Map<@Phone String, String> phoneBooks
) {

    public Map<byte[], PhoneBook> phoneBookMap(
        final Function<byte[], byte[]> hashEncryptionFunction
    ) {
        return phoneBooks.entrySet().stream()
            .map(phoneBook -> new AbstractMap.SimpleEntry<>(
                hashEncryptionFunction.apply(phoneBook.getKey().getBytes(StandardCharsets.UTF_8)),
                new PhoneBook(phoneBook.getKey(), phoneBook.getValue())
            ))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
