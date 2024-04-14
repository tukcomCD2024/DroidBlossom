package site.timecapsulearchive.core.domain.capsule.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.CapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.repository.SecretCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto.MySecreteCapsuleDto;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.MySecretCapsuleSliceResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SecretCapsuleService {

    private final SecretCapsuleQueryRepository secretCapsuleQueryRepository;
    private final CapsuleMapper capsuleMapper;

    /**
     * 멤버 아이디와 마지막 캡슐 생성 날짜를 받아서 내 페이지 비밀 캡슐을 조회한다.
     *
     * @param memberId  캡슐을 생성할 멤버 아이디
     * @param size      페이지 사이즈
     * @param createdAt 마지막 캡슐 생성 날짜
     * @return 내 페이지에서 비밀 캡슐을 조회한다.
     */
    public MySecretCapsuleSliceResponse findSecretCapsuleSliceByMemberId(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        final Slice<MySecreteCapsuleDto> slice = secretCapsuleQueryRepository
            .findSecretCapsuleSliceByMemberIdAndCreatedAt(memberId, size, createdAt);

        return capsuleMapper.mySecretCapsuleDetailSliceToResponse(slice.getContent(),
            slice.hasNext());
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
    public CapsuleDetailResponse findSecretCapsuleDetailById(
        final Long memberId,
        final Long capsuleId
    ) {
        final CapsuleDetailDto dto = secretCapsuleQueryRepository.findSecretCapsuleDetailDtosByMemberIdAndCapsuleId(
                memberId, capsuleId)
            .orElseThrow(CapsuleNotFondException::new);

        if (capsuleNotOpened(dto)) {
            return capsuleMapper.notOpenedCapsuleDetailDtoToResponse(dto);
        }

        return capsuleMapper.capsuleDetailDtoToResponse(dto);
    }

    private boolean capsuleNotOpened(final CapsuleDetailDto dto) {
        if (dto.dueDate() == null) {
            return false;
        }

        return !dto.isOpened() || dto.dueDate().isAfter(ZonedDateTime.now(ZoneOffset.UTC));
    }
}

