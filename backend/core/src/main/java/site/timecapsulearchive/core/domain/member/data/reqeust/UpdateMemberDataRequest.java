package site.timecapsulearchive.core.domain.member.data.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import site.timecapsulearchive.core.domain.member.data.dto.UpdateMemberDataDto;

@Schema(description = "회원 업데이트 포맷")
public record UpdateMemberDataRequest(

    @Schema(description = "닉네임")
    String nickname,

    @Schema(description = "태그")
    String tag
) {

    public UpdateMemberDataDto toDto() {
        return new UpdateMemberDataDto(nickname, tag);
    }

}
