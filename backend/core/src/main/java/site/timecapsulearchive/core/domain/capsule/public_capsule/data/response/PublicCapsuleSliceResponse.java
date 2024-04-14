package site.timecapsulearchive.core.domain.capsule.public_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.Function;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.PublicCapsuleDetailResponse;

@Schema(description = "공개 캡슐 슬라이싱")
public record PublicCapsuleSliceResponse(

    @Schema(description = "공개 캡슐 정보")
    List<PublicCapsuleDetailResponse> publicCapsules,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext
) {

    public static PublicCapsuleSliceResponse createOf(
        final List<CapsuleDetailDto> content,
        final boolean hasNext,
        final Function<String, String> singlePreSignUrlFunction,
        final Function<String, List<String>> multiplePreSignUrlFunction
    ) {
        final List<PublicCapsuleDetailResponse> list = content.stream()
            .map(dto -> PublicCapsuleDetailResponse.createOf(dto, singlePreSignUrlFunction,
                multiplePreSignUrlFunction))
            .toList();

        return new PublicCapsuleSliceResponse(list, hasNext);
    }
}