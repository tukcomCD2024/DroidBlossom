package site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.Function;
import site.timecapsulearchive.core.domain.capsule.data.dto.CapsuleBasicInfoDto;
import site.timecapsulearchive.core.domain.capsule.data.response.CapsuleBasicInfoResponse;

@Schema(description = "사용자가 만든 공개 캡슐 슬라이싱")
public record MyPublicCapsuleSliceResponse(

    @Schema(description = "사용자가 만든 공개 캡슐 정보")
    List<CapsuleBasicInfoResponse> publicCapsules,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext
) {

    public static MyPublicCapsuleSliceResponse createOf(
        final List<CapsuleBasicInfoDto> publicCapsules,
        final boolean hasNext,
        final Function<String, String> singlePreSignUrlFunction
    ) {
        List<CapsuleBasicInfoResponse> publicCapsuleResponses = publicCapsules.stream()
            .map(capsule -> capsule.toResponse(singlePreSignUrlFunction))
            .toList();

        return new MyPublicCapsuleSliceResponse(publicCapsuleResponses, hasNext);
    }
}
