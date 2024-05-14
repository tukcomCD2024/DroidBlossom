package site.timecapsulearchive.core.domain.group.service.write;

import site.timecapsulearchive.core.domain.group.data.dto.GroupCreateDto;

public interface GroupWriteService {

    void createGroup(final Long memberId, final GroupCreateDto dto);

    void inviteGroup(final Long memberId, final Long groupId, final Long targetId);

    void rejectRequestGroup(final Long memberId, final Long groupId, final Long targetId);

    void acceptGroupInvite(final Long memberId, final Long groupId, final Long targetId);

}
