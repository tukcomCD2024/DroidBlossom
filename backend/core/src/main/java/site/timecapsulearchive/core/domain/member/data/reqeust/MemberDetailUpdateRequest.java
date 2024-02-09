package site.timecapsulearchive.core.domain.member.data.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "회원 업데이트 포맷")
public record MemberDetailUpdateRequest(

    @Schema(description = "닉네임")
    String nickname,

    @Schema(description = "프로플 이미지")
    MultipartFile profileImage
) {

}