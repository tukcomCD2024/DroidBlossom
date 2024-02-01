package site.timecapsulearchive.core.domain.capsuleskin.service;

import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.capsuleskin.dto.response.CapsuleSkinsPageResponse;

@Service
@RequiredArgsConstructor
public class CapsuleSkinService {

    public CapsuleSkinsPageResponse findCapsuleSkinSliceByCreatedAt(
        Long memberId,
        Long size,
        ZonedDateTime createdAt
    ) {
        return null;
    }
}
