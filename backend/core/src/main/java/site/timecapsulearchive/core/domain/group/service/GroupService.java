package site.timecapsulearchive.core.domain.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.group.data.mapper.GroupMapper;
import site.timecapsulearchive.core.domain.group.data.reqeust.GroupCreateRequest;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.repository.GroupRepository;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper mapper;

    public void createGroup(final GroupCreateRequest request, final String groupProfileUrl) {
        Group group = mapper.createRequestToEntity(request, groupProfileUrl);

        groupRepository.save(group);
    }
}
