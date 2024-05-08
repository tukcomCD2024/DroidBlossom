package site.timecapsulearchive.core.domain.group.service;

import site.timecapsulearchive.core.domain.group.data.dto.GroupCreateDto;

public interface GroupWriteService {

    void createGroup(final Long memberId, final GroupCreateDto dto);

    void inviteGroup(final Long memberId, final Long groupId, final Long targetId);
}
