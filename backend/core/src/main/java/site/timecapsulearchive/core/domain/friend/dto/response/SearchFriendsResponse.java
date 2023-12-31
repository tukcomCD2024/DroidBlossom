package site.timecapsulearchive.core.domain.friend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import site.timecapsulearchive.core.domain.member.dto.response.MemberSummaryResponse;

@Schema(description = "전화번호 리스트로 찾은 친구")
@Validated
public record SearchFriendsResponse(
    @JsonProperty("friends")
    @Schema(description = "회원 요약 정보")
    @Valid
    List<MemberSummaryResponse> friends
) {

}
