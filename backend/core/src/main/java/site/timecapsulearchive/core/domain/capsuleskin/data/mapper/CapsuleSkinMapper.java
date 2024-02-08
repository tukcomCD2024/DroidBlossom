package site.timecapsulearchive.core.domain.capsuleskin.data.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinSummaryDto;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinSummaryResponse;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinsPageResponse;

@Component
public class CapsuleSkinMapper {

    public CapsuleSkinsPageResponse capsuleSkinSliceToResponse(
        List<CapsuleSkinSummaryDto> content,
        boolean hasNext
    ) {
        List<CapsuleSkinSummaryResponse> responses = content
            .stream()
            .map(this::getCapsuleSkinSummaryResponse)
            .toList();

        return new CapsuleSkinsPageResponse(responses, hasNext);
    }

    private CapsuleSkinSummaryResponse getCapsuleSkinSummaryResponse(CapsuleSkinSummaryDto skin) {
        return CapsuleSkinSummaryResponse.builder()
            .id(skin.id())
            .skinUrl(skin.skinUrl())
            .name(skin.name())
            .createdAt(skin.createdAt())
            .build();
    }
}
