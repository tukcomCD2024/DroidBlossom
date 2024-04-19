package site.timecapsulearchive.core.domain.capsule.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.repository.GroupCapsuleOpenQueryRepository;

@Service
@RequiredArgsConstructor
public class GroupCapsuleOpenService {

    private final GroupCapsuleOpenQueryRepository repository;

    public void bulkSave(final List<Long> groupMemberIds, final Capsule capsule) {
        repository.bulkSave(groupMemberIds, capsule);
    }
}
