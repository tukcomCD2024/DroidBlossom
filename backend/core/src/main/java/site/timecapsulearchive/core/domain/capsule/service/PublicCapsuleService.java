package site.timecapsulearchive.core.domain.capsule.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.exception.NoCapsuleAuthorityException;
import site.timecapsulearchive.core.domain.capsule.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto.PublicCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.repository.PublicCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.CapsuleDetailResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicCapsuleService {

    private final PublicCapsuleQueryRepository publicCapsuleQueryRepository;
    private final CapsuleMapper capsuleMapper;

    public CapsuleDetailResponse findPublicCapsuleDetailByMemberIdAndCapsuleId(Long memberId,
        Long capsuleId) {
        PublicCapsuleDetailDto detailDto = publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
                memberId, capsuleId)
            .orElseThrow(CapsuleNotFondException::new);

        if (!detailDto.isFriend()) {
            throw new NoCapsuleAuthorityException();
        }

        if (capsuleNotOpened(detailDto)) {
            return capsuleMapper.notOpenedPublicCapsuleDetailDtoToResponse(detailDto);
        }

        return capsuleMapper.publicCapsuleDetailDtoToResponse(detailDto);
    }

    private boolean capsuleNotOpened(final PublicCapsuleDetailDto detailDto) {
        if (detailDto.dueDate() == null) {
            return false;
        }

        return !detailDto.isOpened() || detailDto.dueDate()
            .isAfter(ZonedDateTime.now(ZoneOffset.UTC));
    }
}
