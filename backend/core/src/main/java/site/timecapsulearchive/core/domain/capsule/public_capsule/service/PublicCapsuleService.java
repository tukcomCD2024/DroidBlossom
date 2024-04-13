package site.timecapsulearchive.core.domain.capsule.public_capsule.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.public_capsule.repository.PublicCapsuleQueryRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicCapsuleService {

    private final PublicCapsuleQueryRepository publicCapsuleQueryRepository;

    public CapsuleDetailDto findPublicCapsuleDetailByMemberIdAndCapsuleId(
        final Long memberId,
        final Long capsuleId
    ) {
        final CapsuleDetailDto detailDto = publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
                memberId, capsuleId)
            .orElseThrow(CapsuleNotFondException::new);

        if (capsuleNotOpened(detailDto)) {
            return detailDto.excludeTitleAndContentAndImagesAndVideos();
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
        final Long memberId,
        final Long capsuleId
    ) {
        return publicCapsuleQueryRepository.findPublicCapsuleSummaryDtosByMemberIdAndCapsuleId(
                memberId, capsuleId)
            .orElseThrow(CapsuleNotFondException::new);
    }

    public Slice<CapsuleDetailDto> findPublicCapsulesMadeByFriend(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return publicCapsuleQueryRepository.findPublicCapsulesDtoMadeByFriend(memberId, size,
            createdAt);
    }
}
