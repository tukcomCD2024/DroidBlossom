package site.timecapsulearchive.core.domain.capsule.public_capsule.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto.MyPublicCapsuleDto;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto.PublicCapsuleDetailDto;
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

    public Slice<PublicCapsuleDetailDto> findPublicCapsulesMadeByFriend(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return publicCapsuleQueryRepository.findPublicCapsulesDtoMadeByFriend(memberId, size,
            createdAt);
    }

    /**
     * 사용자가 생성한 공개 캡슐 목록을 반환한다
     *
     * @param memberId  조회할 사용자
     * @param size      조회할 캡슐의 개수
     * @param createdAt 조회를 시작할 캡슐의 생성 시간, 첫 조회라면 현재 시간, 이후 조회라면 맨 마지막 데이터의 시간
     * @return 사용자가 생성한 공개 캡슐 목록
     */
    public Slice<MyPublicCapsuleDto> findMyPublicCapsuleSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return publicCapsuleQueryRepository.findMyPublicCapsuleSlice(memberId, size, createdAt);
    }
}
