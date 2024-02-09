package site.timecapsulearchive.core.domain.friend.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import site.timecapsulearchive.core.domain.member.data.response.MemberSummaryResponse;

@Schema(description = "친구 리스트 페이징")
public record FriendsPageResponse(

    @Schema(description = "회원 요약 정보 리스트")
    List<MemberSummaryResponse> friends,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext,

    @Schema(description = "이전 페이지유무")
    Boolean hasPrevious
) {

}