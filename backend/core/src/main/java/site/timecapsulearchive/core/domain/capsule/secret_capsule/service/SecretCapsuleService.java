package site.timecapsulearchive.core.domain.capsule.secret_capsule.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.repository.SecretCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto.MySecreteCapsuleDto;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SecretCapsuleService {

    private final SecretCapsuleQueryRepository secretCapsuleQueryRepository;

    /**
     * 멤버 아이디와 마지막 캡슐 생성 날짜를 받아서 내 페이지 비밀 캡슐을 조회한다.
     *
     * @param memberId  캡슐을 생성할 멤버 아이디
     * @param size      페이지 사이즈
     * @param createdAt 마지막 캡슐 생성 날짜
     * @return 내 페이지에서 비밀 캡슐을 조회한다.
     */
    public Slice<MySecreteCapsuleDto> findSecretCapsuleSliceByMemberId(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return secretCapsuleQueryRepository
            .findSecretCapsuleSliceByMemberIdAndCreatedAt(memberId, size, createdAt);
    }

    /**
     * 멤버 아이디와 캡슐 아이디를 받아서 캡슐 요약 정보를 반환한다.
     *
     * @param memberId  멤버 아이디
     * @param capsuleId 캡슐 아이디
     * @return 캡슐 요약 정보
     */
    public CapsuleSummaryDto findSecretCapsuleSummaryById(
        final Long memberId,
        final Long capsuleId
    ) {
        return secretCapsuleQueryRepository.findSecretCapsuleSummaryDtosByMemberIdAndCapsuleId(
                memberId, capsuleId)
            .orElseThrow(CapsuleNotFondException::new);
    }

    /**
     * 멤버 아이디와 캡슐 아이디를 받아서 캡슐 상세 정보를 반환한다.
     *
     * @param memberId  멤버 아이디
     * @param capsuleId 캡슐 아이디
     * @return 캡슐 상세 정보
     */
    public CapsuleDetailDto findSecretCapsuleDetailById(
        final Long memberId,
        final Long capsuleId
    ) {
        final CapsuleDetailDto detailDto = secretCapsuleQueryRepository.findSecretCapsuleDetailDtosByMemberIdAndCapsuleId(
                memberId, capsuleId)
            .orElseThrow(CapsuleNotFondException::new);

        if (capsuleNotOpened(detailDto)) {
            return detailDto.excludeTitleAndContentAndImagesAndVideos();
        }

        return detailDto;
    }

    private boolean capsuleNotOpened(final CapsuleDetailDto dto) {
        if (dto.dueDate() == null) {
            return false;
        }

        return !dto.isOpened() || dto.dueDate().isAfter(ZonedDateTime.now(ZoneOffset.UTC));
    }
}

