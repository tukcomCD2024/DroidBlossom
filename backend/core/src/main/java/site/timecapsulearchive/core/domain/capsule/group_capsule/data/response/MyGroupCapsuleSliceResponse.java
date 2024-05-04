package site.timecapsulearchive.core.domain.capsule.group_capsule.data.response;

import java.util.List;
import java.util.function.Function;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.MyGroupCapsuleDto;

public record MyGroupCapsuleSliceResponse(
    List<MyGroupCapsuleResponse> groupCapsules,
    Boolean hasNext
) {

    public static MyGroupCapsuleSliceResponse createOf(
        final List<MyGroupCapsuleDto> groupCapsules,
        final boolean hasNext,
        final Function<String, String> singlePreSignUrlFunction
    ) {
        List<MyGroupCapsuleResponse> groupCapsuleResponses = groupCapsules.stream()
            .map(capsule -> capsule.toResponse(singlePreSignUrlFunction))
            .toList();

        return new MyGroupCapsuleSliceResponse(groupCapsuleResponses, hasNext);
    }
}
