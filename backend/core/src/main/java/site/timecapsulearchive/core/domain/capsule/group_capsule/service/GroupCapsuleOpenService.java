package site.timecapsulearchive.core.domain.capsule.group_capsule.service;

import jakarta.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleOpenRepository;

@Service
@RequiredArgsConstructor
public class GroupCapsuleOpenService {

    private final GroupCapsuleOpenRepository groupCapsuleOpenRepository;

    public void bulkSave(
        final Long groupId,
        final List<Long> groupMemberIds,
        final Capsule capsule
    ) {
        groupCapsuleOpenRepository.bulkSave(groupId, groupMemberIds, capsule);
    }

    @Transactional
    public void deleteByMemberId(final Long memberId, final ZonedDateTime deletedAt) {
        groupCapsuleOpenRepository.deleteByMemberId(memberId, deletedAt);
    }
}
