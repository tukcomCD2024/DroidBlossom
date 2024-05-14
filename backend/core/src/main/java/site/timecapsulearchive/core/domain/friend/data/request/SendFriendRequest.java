package site.timecapsulearchive.core.domain.friend.data.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(description = "다수의 친구들에게 친구 추가 요청")
public record SendFriendRequest(

    @Schema(description = "추가하고 싶은 친구들")
    @NotNull
    @Size(min = 1, max = 30)
    List<Long> friendIds
) {

}
