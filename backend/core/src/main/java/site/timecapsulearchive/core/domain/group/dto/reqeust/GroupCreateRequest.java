package site.timecapsulearchive.core.domain.group.dto.reqeust;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "그룹 생성 포맷")
@Validated
public record GroupCreateRequest(
    @JsonProperty("name")
    @Schema(description = "그룹 이름")
    String name,

    @JsonProperty("profileImage")
    @Schema(description = "그룹 이미지")
    MultipartFile profileImage,

    @JsonProperty("description")
    @Schema(description = "그룹 설명")
    String description,

    @JsonProperty("memberIds")
    @Schema(description = "그룹원 아이디들")
    @Valid
    List<Long> memberIds
) {

}