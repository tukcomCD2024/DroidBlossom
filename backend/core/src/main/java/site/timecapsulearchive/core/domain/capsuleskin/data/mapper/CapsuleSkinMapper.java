package site.timecapsulearchive.core.domain.capsuleskin.data.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinSummaryDto;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinSummaryResponse;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinsSliceResponse;

@Component
public class CapsuleSkinMapper {

    public CapsuleSkinsSliceResponse capsuleSkinSliceToResponse(
        final List<CapsuleSkinSummaryDto> content,
        final boolean hasNext
    ) {
        final List<CapsuleSkinSummaryResponse> responses = content
            .stream()
            .map(this::getCapsuleSkinSummaryResponse)
            .toList();

        return new CapsuleSkinsSliceResponse(responses, hasNext);
    }

    private CapsuleSkinSummaryResponse getCapsuleSkinSummaryResponse(
        final CapsuleSkinSummaryDto skin) {
        return CapsuleSkinSummaryResponse.builder()
            .id(skin.id())
            .skinUrl(skin.skinUrl())
            .name(skin.name())
            .createdAt(skin.createdAt())
            .build();
    }
}
