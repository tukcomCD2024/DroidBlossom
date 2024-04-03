package site.timecapsulearchive.core.domain.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.repository.GroupRepository;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    public void createGroup(final Group group) {
        groupRepository.save(group);
    }
}
