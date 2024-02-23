package site.timecapsulearchive.core.domain.capsule.dto.group_c.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "그룹 캡슐 생성 포맷")
public record GroupCapsuleCreateRequest(

    @Schema(description = "미디어들(이미지, 비디오)")
    List<MultipartFile> media,

    @Schema(description = "캡슐 스킨 아이디")
    Long capsuleSkinId,

    @Schema(description = "제목")
    String title,

    @Schema(description = "내용")
    String content,

    @Schema(description = "경도")
    Float longitude,

    @Schema(description = "위도")
    Float latitude,

    @Schema(description = "생성일")
    ZonedDateTime dueDate
) {

}