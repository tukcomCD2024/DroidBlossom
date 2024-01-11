package site.timecapsulearchive.core.domain.group.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Schema(description = "그룹 리스트 페이징")
@Validated
public record GroupsPageResponse(

    @Schema(description = "그룹 리스트")
    @Valid
    List<GroupSummaryResponse> groups,

    @Schema(description = "다음 페이지 유무")

    Boolean hasNext,

    @Schema(description = "이전 페이지 유무")

    Boolean hasPrevious
) {

}
