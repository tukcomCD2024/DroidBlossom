package site.timecapsulearchive.core.domain.capsuleskin.data.mapper;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinCreateDto;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinMessageDto;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinSummaryDto;
import site.timecapsulearchive.core.domain.capsuleskin.data.reqeust.CapsuleSkinCreateRequest;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinSummaryResponse;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinsSliceResponse;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;
import site.timecapsulearchive.core.infra.s3.manager.S3UrlGenerator;

@Component
@RequiredArgsConstructor
public class CapsuleSkinMapper {

    private final S3UrlGenerator s3UrlGenerator;
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
            .skinUrl(s3PreSignedUrlManager.getS3PreSignedUrlForGet(skin.skinUrl()))
            .name(skin.name())
            .createdAt(skin.createdAt())
            .build();
    }

    public CapsuleSkinCreateDto createRequestToDto(final CapsuleSkinCreateRequest request) {
        return new CapsuleSkinCreateDto(
            request.skinName(),
            request.imageUrl(),
            request.directory(),
            request.motionName(),
            request.retarget()
        );
    }

    public CapsuleSkin createDtoToEntity(CapsuleSkinCreateDto dto, Member member) {
        return CapsuleSkin.builder()
            .skinName(dto.skinName())
            .imageUrl(
                S3UrlGenerator.generateFileName(member.getId(), dto.directory(), dto.imageUrl()))
            .member(member)
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
            .imageUrl(
                s3PreSignedUrlManager.getS3PreSignedUrlForGet(
                    S3UrlGenerator.generateFileName(memberId, dto.directory(), dto.imageUrl())
                ))
            .motionName(dto.motionName())
            .retarget(dto.retarget())
            .build();
    }
}
