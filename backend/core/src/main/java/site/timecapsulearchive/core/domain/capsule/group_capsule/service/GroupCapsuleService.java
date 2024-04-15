package site.timecapsulearchive.core.domain.capsule.group_capsule.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleCreateRequestDto;
import site.timecapsulearchive.core.domain.capsule.repository.CapsuleRepository;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupCapsuleService {

    private final CapsuleRepository capsuleRepository;

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
}
