package site.timecapsulearchive.core.domain.capsule.group_capsule.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.CapsuleRepository;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleCreateRequestDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupCapsuleService {

    private final CapsuleRepository capsuleRepository;
    private final GroupCapsuleQueryRepository groupCapsuleQueryRepository;

    @Transactional
    public Capsule saveGroupCapsule(
        final GroupCapsuleCreateRequestDto dto,
        final Member member,
        final Group group,
        final CapsuleSkin capsuleSkin,
        final Point point
    ) {
        final Capsule capsule = dto.toGroupCapsule(member, capsuleSkin, group,
            CapsuleType.GROUP, point);

        capsuleRepository.save(capsule);

        return capsule;
    }

    public GroupCapsuleDetailDto findGroupCapsuleDetailByGroupIDAndCapsuleId(
        final Long capsuleId
    ) {
        final GroupCapsuleDetailDto detailDto = groupCapsuleQueryRepository.findGroupCapsuleDetailDtoByCapsuleId(
                capsuleId)
            .orElseThrow(CapsuleNotFondException::new);

        if (capsuleNotOpened(detailDto)) {
            return detailDto.excludeDetailContents();
        }

        return detailDto;
    }

    private boolean capsuleNotOpened(final GroupCapsuleDetailDto detailDto) {
        if (detailDto.capsuleDetailDto().dueDate() == null) {
            return false;
        }

        return !detailDto.capsuleDetailDto().isOpened() || detailDto.capsuleDetailDto().dueDate()
            .isAfter(ZonedDateTime.now(ZoneOffset.UTC));
    }

    public GroupCapsuleSummaryDto findGroupCapsuleSummaryByGroupIDAndCapsuleId(
        final Long capsuleId) {
        return groupCapsuleQueryRepository.findGroupCapsuleSummaryDtoByCapsuleId(capsuleId)
            .orElseThrow(CapsuleNotFondException::new);
    }
}
