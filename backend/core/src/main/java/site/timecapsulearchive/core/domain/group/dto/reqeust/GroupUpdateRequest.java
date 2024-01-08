package site.timecapsulearchive.core.domain.group.dto.reqeust;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "그룹 업데이트 포맷")
@Validated
public record GroupUpdateRequest(
    @JsonProperty("name")
    String name,

    @JsonProperty("description")
    String description,

    @JsonProperty("profileImage")
    MultipartFile profileImage
) {

}
