package site.timecapsulearchive.core.domain.friend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import site.timecapsulearchive.core.domain.member.dto.response.MemberSummaryResponse;

@Schema(description = "친구 리스트 페이징")
@Validated
public record FriendsPageResponse(
    @JsonProperty("friends")
    @Schema(description = "회원 요약 정보 리스트")
    @Valid
    List<MemberSummaryResponse> friends,

    @JsonProperty("hasNext")
    @Schema(description = "다음 페이지 유무")
    Boolean hasNext,

    @JsonProperty("hasPrevious")
    @Schema(description = "이전 페이지유무")
    Boolean hasPrevious
) {

}