package site.timecapsulearchive.core.domain.capsuleskin.service;

import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinSummaryDto;
import site.timecapsulearchive.core.domain.capsuleskin.data.mapper.CapsuleSkinMapper;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinsPageResponse;
import site.timecapsulearchive.core.domain.capsuleskin.repository.CapsuleSkinQueryRepository;

@Service
@RequiredArgsConstructor
public class CapsuleSkinService {

    private final CapsuleSkinQueryRepository capsuleSkinQueryRepository;
    private final CapsuleSkinMapper capsuleSkinMapper;

    public CapsuleSkinsPageResponse findCapsuleSkinSliceByCreatedAtAndMemberId(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        Slice<CapsuleSkinSummaryDto> slice = capsuleSkinQueryRepository.findCapsuleSkinSliceByCreatedAtAndMemberId(
            memberId,
            size,
            createdAt
        );

        return capsuleSkinMapper.capsuleSkinSliceToResponse(slice.getContent(), slice.hasNext());
    }
}
