package site.timecapsulearchive.core.domain.capsuleskin.repository;

import java.time.ZonedDateTime;
import org.springframework.data.domain.Slice;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinSummaryDto;

public interface CapsuleSkinQueryRepository {

    Slice<CapsuleSkinSummaryDto> findCapsuleSkinSliceByCreatedAtAndMemberId(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    );
}
