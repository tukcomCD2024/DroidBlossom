package site.timecapsulearchive.core.domain.group.dto.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "그룹 업데이트 포맷")
public record GroupUpdateRequest(

    String name,

    String description,

    MultipartFile profileImage
) {

}
