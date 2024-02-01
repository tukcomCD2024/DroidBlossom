package site.timecapsulearchive.core.domain.capsuleskin.service;

import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.capsuleskin.dto.CapsuleSkinSummaryDto;
import site.timecapsulearchive.core.domain.capsuleskin.dto.CapsuleSkinsPageDto;
import site.timecapsulearchive.core.domain.capsuleskin.repository.CapsuleSkinQueryRepository;

@Service
@RequiredArgsConstructor
public class CapsuleSkinService {

    private final CapsuleSkinQueryRepository capsuleSkinQueryRepository;

    public CapsuleSkinsPageDto findCapsuleSkinSliceByCreatedAt(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        Slice<CapsuleSkinSummaryDto> slice = capsuleSkinQueryRepository.findCapsuleSkinSliceByCreatedAtAndMemberId(
            memberId,
            size,
            createdAt
        );

        return CapsuleSkinsPageDto.from(slice.getContent(), slice.hasNext());
    }
}
