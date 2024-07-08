package site.timecapsulearchive.core.domain.capsule.group_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleDto;

@Schema(description = "그룹 캡슐 목록 응답")
public record GroupCapsuleSliceResponse(

    @Schema(description = "그룹 캡슐 목록")
    List<GroupCapsuleResponse> groupCapsuleResponses,

    @Schema(description = "다음 페이지 유무")
    boolean hasNext
) {

    public static GroupCapsuleSliceResponse createOf(
        List<GroupCapsuleDto> content,
        boolean hasNext,
        UnaryOperator<String> singlePreSignUrlFunction,
        Function<String, List<String>> multiplePreSignUrlFunction,
        UnaryOperator<Point> pointTransformFunction
    ) {
        List<GroupCapsuleResponse> responses = content.stream()
            .map(dto -> dto.toResponse(singlePreSignUrlFunction, multiplePreSignUrlFunction,
                pointTransformFunction))
            .toList();

        return new GroupCapsuleSliceResponse(responses, hasNext);
    }
}
