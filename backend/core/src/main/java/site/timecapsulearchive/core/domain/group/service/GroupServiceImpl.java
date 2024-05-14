package site.timecapsulearchive.core.domain.group.service;

import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.group.data.dto.GroupCreateDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupDetailDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupSummaryDto;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.service.read.GroupReadService;
import site.timecapsulearchive.core.domain.group.service.write.GroupWriteService;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupReadService groupReadService;
    private final GroupWriteService groupWriteService;

    @Override
    public Group findGroupById(final Long groupId) {
        return groupReadService.findGroupById(groupId);
    }

    @Override
    public Slice<GroupSummaryDto> findGroupsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return groupReadService.findGroupsSlice(memberId, size, createdAt);
    }

    @Override
    public GroupDetailDto findGroupDetailByGroupId(final Long memberId, final Long groupId) {
        return groupReadService.findGroupDetailByGroupId(memberId, groupId);
    }

    @Override
    public void createGroup(final Long memberId, final GroupCreateDto dto) {
        groupWriteService.createGroup(memberId, dto);
    }

    @Override
    public void inviteGroup(final Long memberId, final Long groupId, final Long targetId) {
        groupWriteService.inviteGroup(memberId, groupId, targetId);
    }

    @Override
    public void rejectRequestGroup(final Long groupMemberId, final Long groupId,
        final Long targetId) {
        groupWriteService.rejectRequestGroup(groupMemberId, groupId, targetId);
    }

    @Override
    public void acceptGroupInvite(final Long memberId, final Long groupId, final Long targetId) {
        groupWriteService.acceptGroupInvite(memberId, groupId, targetId);
    }

    @Override
    public void deleteGroup(final Long memberId, final Long groupId) {
        groupWriteService.deleteGroup(memberId, groupId);
    }
}
