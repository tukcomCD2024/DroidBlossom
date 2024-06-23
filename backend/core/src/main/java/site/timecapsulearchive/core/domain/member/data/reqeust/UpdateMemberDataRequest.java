package site.timecapsulearchive.core.domain.member.data.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import site.timecapsulearchive.core.domain.member.data.dto.UpdateMemberDataDto;

@Schema(description = "회원 업데이트 포맷")
public record UpdateMemberDataRequest(

    @Schema(description = "닉네임")
    @NotNull(message = "사용자 닉네임은 필수 입니다.")
    String nickname,

    @Schema(description = "태그")
    @NotNull(message = "사용자 태그는 필수 입니다.")
    String tag
) {

    public UpdateMemberDataDto toDto() {
        return new UpdateMemberDataDto(nickname, tag);
    }

}
