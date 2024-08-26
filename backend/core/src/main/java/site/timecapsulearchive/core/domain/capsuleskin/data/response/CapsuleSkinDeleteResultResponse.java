package site.timecapsulearchive.core.domain.capsuleskin.data.response;

import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinDeleteResult;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinDeleteResultDto;

public record CapsuleSkinDeleteResultResponse(
    CapsuleSkinDeleteResult capsuleSkinDeleteResult,
    String message
) {

    public static CapsuleSkinDeleteResultResponse createOf(
        CapsuleSkinDeleteResultDto capsuleSkinDeleteResultDto) {
        return new CapsuleSkinDeleteResultResponse(
            capsuleSkinDeleteResultDto.capsuleSkinDeleteResult(),
            capsuleSkinDeleteResultDto.message()
        );
    }
}
