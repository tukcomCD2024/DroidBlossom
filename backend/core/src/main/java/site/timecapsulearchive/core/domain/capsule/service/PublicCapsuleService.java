package site.timecapsulearchive.core.domain.capsule.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.repository.PublicCapsuleQueryRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicCapsuleService {

    private final PublicCapsuleQueryRepository publicCapsuleQueryRepository;

    public CapsuleDetailDto findPublicCapsuleDetailByMemberIdAndCapsuleId(
        Long memberId,
        Long capsuleId
    ) {
        CapsuleDetailDto detailDto = publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
                capsuleId, memberId)
            .orElseThrow(CapsuleNotFondException::new);

        if (capsuleNotOpened(detailDto)) {
            return detailDto.removeContent();
        }

        return detailDto;
    }

    private boolean capsuleNotOpened(final CapsuleDetailDto detailDto) {
        if (detailDto.dueDate() == null) {
            return false;
        }

        return !detailDto.isOpened() || detailDto.dueDate()
            .isAfter(ZonedDateTime.now(ZoneOffset.UTC));
    }

    public CapsuleSummaryDto findPublicCapsuleSummaryByMemberIdAndCapsuleId(
        Long memberId,
        Long capsuleId
    ) {
        return publicCapsuleQueryRepository.findPublicCapsuleSummaryDtosByMemberIdAndCapsuleId(
                memberId, capsuleId)
            .orElseThrow(CapsuleNotFondException::new);
    }
}
