package site.timecapsulearchive.core.domain.group.data.mapper;

import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.group.data.reqeust.GroupCreateRequest;
import site.timecapsulearchive.core.domain.group.entity.Group;

@Component
public class GroupMapper {

    public Group createRequestToEntity(final GroupCreateRequest request, final String groupProfileUrl) {
        return Group.builder()
            .groupName(request.name())
            .groupDescription(request.description())
            .groupProfileUrl(groupProfileUrl)
            .build();
    }

}
