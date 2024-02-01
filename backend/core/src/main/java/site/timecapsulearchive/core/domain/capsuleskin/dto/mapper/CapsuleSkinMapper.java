package site.timecapsulearchive.core.domain.capsuleskin.dto.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsuleskin.dto.CapsuleSkinSummaryDto;
import site.timecapsulearchive.core.domain.capsuleskin.dto.CapsuleSkinsPageDto;
import site.timecapsulearchive.core.domain.capsuleskin.dto.response.CapsuleSkinSummaryResponse;
import site.timecapsulearchive.core.domain.capsuleskin.dto.response.CapsuleSkinsPageResponse;

@Component
public class CapsuleSkinMapper {

    public CapsuleSkinsPageResponse capsuleSkinPageDtoToResponse(CapsuleSkinsPageDto responseDto) {
        List<CapsuleSkinSummaryResponse> responses = responseDto.capsuleSkins()
            .stream()
            .map(this::getCapsuleSkinSummaryResponse)
            .toList();

        return new CapsuleSkinsPageResponse(responses, responseDto.hasNext());
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
