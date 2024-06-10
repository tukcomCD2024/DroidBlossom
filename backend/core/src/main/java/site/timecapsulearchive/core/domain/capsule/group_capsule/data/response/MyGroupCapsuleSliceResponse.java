package site.timecapsulearchive.core.domain.capsule.group_capsule.data.response;

import java.util.List;
import java.util.function.Function;
import site.timecapsulearchive.core.domain.capsule.data.dto.CapsuleBasicInfoDto;
import site.timecapsulearchive.core.domain.capsule.data.response.CapsuleBasicInfoResponse;

public record MyGroupCapsuleSliceResponse(
    List<CapsuleBasicInfoResponse> groupCapsules,
    Boolean hasNext
) {

    public static MyGroupCapsuleSliceResponse createOf(
        final List<CapsuleBasicInfoDto> groupCapsules,
        final boolean hasNext,
        final Function<String, String> singlePreSignUrlFunction
    ) {
        List<CapsuleBasicInfoResponse> groupCapsuleResponses = groupCapsules.stream()
            .map(capsule -> capsule.toResponse(singlePreSignUrlFunction))
            .toList();

        return new MyGroupCapsuleSliceResponse(groupCapsuleResponses, hasNext);
    }
}
