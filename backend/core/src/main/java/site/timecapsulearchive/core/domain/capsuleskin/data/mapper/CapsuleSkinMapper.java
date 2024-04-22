package site.timecapsulearchive.core.domain.capsuleskin.data.mapper;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinCreateDto;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinMessageDto;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinSummaryDto;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinSummaryResponse;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinsSliceResponse;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@Component
@RequiredArgsConstructor
public class CapsuleSkinMapper {

    private final S3PreSignedUrlManager s3PreSignedUrlManager;

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
            .skinUrl(s3PreSignedUrlManager.preSignImageForGet(skin.skinUrl()))
            .name(skin.name())
            .createdAt(skin.createdAt())
            .build();
    }

    public CapsuleSkinMessageDto createDtoToMessageDto(
        final Long memberId,
        final String memberName,
        final CapsuleSkinCreateDto dto
    ) {
        return CapsuleSkinMessageDto.builder()
            .memberId(memberId)
            .memberName(memberName)
            .skinName(dto.skinName())
            .imageUrl(dto.imageFullPath())
            .motionName(dto.motionName())
            .retarget(dto.retarget())
            .build();
    }
}
