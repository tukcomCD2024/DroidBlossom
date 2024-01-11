package site.timecapsulearchive.core.domain.history.dto.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Schema(description = "히스토리 생성 요청")
@Validated
public record HistoryCreateRequest(

    @Schema(description = "제목")
    String title,

    @Schema(description = "이미지 아이디들")
    @Valid
    List<Long> imageIds
) {

}
