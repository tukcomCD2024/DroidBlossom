package site.timecapsulearchive.core.domain.capsuleskin.service;

import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.capsuleskin.dto.response.CapsuleSkinSummaryResponse;
import site.timecapsulearchive.core.domain.capsuleskin.dto.response.CapsuleSkinsPageResponse;
import site.timecapsulearchive.core.domain.capsuleskin.repository.CapsuleSkinQueryRepository;

@Service
@RequiredArgsConstructor
public class CapsuleSkinService {

    private final CapsuleSkinQueryRepository capsuleSkinQueryRepository;

    public CapsuleSkinsPageResponse findCapsuleSkinSliceByCreatedAt(
        Long memberId,
        int size,
        ZonedDateTime createdAt
    ) {
        Slice<CapsuleSkinSummaryResponse> slice = capsuleSkinQueryRepository.findCapsuleSkinSliceByCreatedAtAndMemberId(
            memberId,
            size,
            createdAt
        );

        return CapsuleSkinsPageResponse.from(slice.getContent(), slice.hasNext());
    }
}
