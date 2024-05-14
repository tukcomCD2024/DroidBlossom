package site.timecapsulearchive.core.domain.group.service.write;

import site.timecapsulearchive.core.domain.group.data.dto.GroupCreateDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupUpdateDto;

public interface GroupWriteService {

    void createGroup(final Long memberId, final GroupCreateDto dto);

    void inviteGroup(final Long memberId, final Long groupId, final Long targetId);

    void rejectRequestGroup(final Long memberId, final Long groupId, final Long targetId);

    void acceptGroupInvite(final Long memberId, final Long groupId, final Long targetId);

    void deleteGroup(final Long memberId, final Long groupId);

    void quitGroup(final Long memberId, final Long groupId);

    void kickGroupMember(final Long groupOwnerId, final Long groupId, final Long groupMemberId);

    void updateGroup(final Long memberId, final Long groupId, final GroupUpdateDto dto);
}
