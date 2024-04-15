package site.timecapsulearchive.core.domain.friend.data.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import site.timecapsulearchive.core.global.common.valid.annotation.Phone;

@Schema(description = "전화번호 리스트로 친구 찾기 요청")
public record SearchFriendsRequest(

    @Schema(description = "전화번호부")
    Map<@Phone String, String> phoneBooks
) {

}
