package site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Slice;
import site.timecapsulearchive.core.domain.capsule.data.dto.CapsuleBasicInfoDto;
import site.timecapsulearchive.core.domain.capsule.data.response.CapsuleBasicInfoResponse;

@Schema(description = "내가 생성한 비밀 캡슐 목록 응답")
public record MySecretCapsuleSliceResponse(

    @Schema(description = "내 비밀 캡슐 리스트")
    List<CapsuleBasicInfoResponse> capsules,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext
) {

    public static MySecretCapsuleSliceResponse createOf(
        final Slice<CapsuleBasicInfoDto> dtos,
        final Function<String, String> preSignUrlFunction
    ) {
        final List<CapsuleBasicInfoResponse> capsules = dtos.getContent().
            stream()
            .map(dto -> dto.toResponse(preSignUrlFunction))
            .toList();

        return new MySecretCapsuleSliceResponse(capsules, dtos.hasNext());
    }

}
