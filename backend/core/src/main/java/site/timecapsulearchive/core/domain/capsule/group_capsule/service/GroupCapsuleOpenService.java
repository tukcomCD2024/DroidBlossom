package site.timecapsulearchive.core.domain.capsule.group_capsule.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleOpenQueryRepository;

@Service
@RequiredArgsConstructor
public class GroupCapsuleOpenService {

    private final GroupCapsuleOpenQueryRepository repository;

    public void bulkSave(
        final Long groupId,
        final List<Long> groupMemberIds,
        final Capsule capsule
    ) {
        repository.bulkSave(groupId, groupMemberIds, capsule);
    }
}
