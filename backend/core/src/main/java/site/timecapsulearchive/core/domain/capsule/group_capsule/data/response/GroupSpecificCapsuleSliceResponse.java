package site.timecapsulearchive.core.domain.capsule.group_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.UnaryOperator;
import site.timecapsulearchive.core.domain.capsule.data.dto.CapsuleBasicInfoDto;
import site.timecapsulearchive.core.domain.capsule.data.response.CapsuleBasicInfoResponse;

@Schema(description = "그룹 소유의 그룹 캡슐 목록 응답")
public record GroupSpecificCapsuleSliceResponse(

    @Schema(description = "캡슐 기본 정보 목록")
    List<CapsuleBasicInfoResponse> capsuleBasicInfos,

    @Schema(description = "다음 페이지 유무")
    boolean hasNext
) {

    public static GroupSpecificCapsuleSliceResponse create(
        final List<CapsuleBasicInfoDto> dtos,
        final boolean hasNext,
        final UnaryOperator<String> preSignUrlFunction
    ) {
        List<CapsuleBasicInfoResponse> capsuleBasicInfoResponses = dtos.stream()
            .map(dto -> dto.toResponse(preSignUrlFunction))
            .toList();

        return new GroupSpecificCapsuleSliceResponse(capsuleBasicInfoResponses, hasNext);
    }
}
