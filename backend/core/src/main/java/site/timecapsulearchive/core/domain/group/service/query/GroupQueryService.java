package site.timecapsulearchive.core.domain.group.service.query;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.group.data.dto.GroupDetailDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupDetailTotalDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupSummaryDto;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.exception.GroupNotFoundException;
import site.timecapsulearchive.core.domain.group.repository.GroupRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupQueryService {

    private final GroupRepository groupRepository;

    public Group findGroupById(final Long groupId) {
        return groupRepository.findGroupById(groupId)
            .orElseThrow(GroupNotFoundException::new);
    }

    public Slice<GroupSummaryDto> findGroupsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return groupRepository.findGroupsSlice(memberId, size, createdAt);
    }

    public GroupDetailTotalDto findGroupDetailByGroupId(final Long memberId, final Long groupId) {
        final GroupDetailDto groupDetailDto = groupRepository.findGroupDetailByGroupIdAndMemberId(groupId,
            memberId).orElseThrow(GroupNotFoundException::new);

        final Long groupCapsuleCount = groupRepository.findGroupCapsuleCount(groupId);
        final Boolean canGroupEdit = groupRepository.findGroupEditPermission(groupId, memberId);

        final List<Long> groupMemberIds = groupDetailDto.members().stream()
            .map(GroupMemberDto::memberId)
            .toList();
        final List<Long> friendIds = groupRepository.findFriendIds(groupMemberIds, memberId);

        return GroupDetailTotalDto.as(groupDetailDto, groupCapsuleCount, canGroupEdit, friendIds);
    }
}
