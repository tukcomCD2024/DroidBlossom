package site.timecapsulearchive.core.domain.capsule.data.secret_c.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


@Schema(description = "비밀 캡슐 업데이트 포맷")
public record SecretCapsuleUpdateRequest(

    @Schema(description = "제목")
    String title,

    @Schema(description = "내용")
    String content,

    @Schema(description = "미디어들(이미지, 동영상)")
    List<MultipartFile> media,

    @Schema(description = "개봉일")
    ZonedDateTime dueDate,

    @Schema(description = "캡슐 스킨 아이디")
    Long capsuleSkinId
) {

}