package site.timecapsulearchive.core.domain.capsule.dto.secret_c.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "비밀 캡슐 페이징")
public record SecretCapsulePageResponse(

    @Schema(description = "캡슐 요약 정보 리스트")
    List<SecretCapsuleSummaryResponse> capsules,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext
) {


    public static SecretCapsulePageResponse of(
        List<SecretCapsuleSummaryResponse> summaryResponses,
        boolean hasNext
    ) {
        return new SecretCapsulePageResponse(
            summaryResponses,
            hasNext
        );
    }
}
