package site.timecapsulearchive.core.domain.friend.dto.reqeust;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Schema(description = "전화번호 리스트로 친구 찾기 요청")
@Validated
public record SearchFriendsRequest(
    @JsonProperty("phones")
    @Schema(description = "전화번호 리스트")
    @Valid
    List<String> phones
) {

}
