package site.timecapsulearchive.core.domain.group.data.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "그룹 생성 포맷")
public record GroupCreateRequest(

    @Schema(description = "그룹 이름")
    String name,

    @Schema(description = "그룹 이미지")
    MultipartFile profileImage,

    @Schema(description = "그룹 설명")
    String description,

    @Schema(description = "그룹원 아이디들")
    List<Long> memberIds
) {

}