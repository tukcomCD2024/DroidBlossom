package site.timecapsulearchive.core.domain.group.service.query;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.friend.repository.member_friend.MemberFriendRepository;
import site.timecapsulearchive.core.domain.group.data.dto.FinalGroupSummaryDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupDetailDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupDetailTotalDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberDto;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.exception.GroupNotFoundException;
import site.timecapsulearchive.core.domain.group.repository.GroupRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupQueryService {

    private final GroupRepository groupRepository;
    private final GroupCapsuleQueryRepository groupCapsuleQueryRepository;
    private final MemberFriendRepository memberFriendRepository;

    public Group findGroupById(final Long groupId) {
        return groupRepository.findGroupById(groupId)
            .orElseThrow(GroupNotFoundException::new);
    }

    public Slice<FinalGroupSummaryDto> findGroupsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return groupRepository.findGroupsSlice(memberId, size, createdAt);
    }

    public GroupDetailTotalDto findGroupDetailByGroupId(final Long memberId, final Long groupId) {
        final GroupDetailDto groupDetailDto = groupRepository.findGroupDetailByGroupIdAndMemberId(
            groupId, memberId).orElseThrow(GroupNotFoundException::new);

        final Long groupCapsuleCount = groupCapsuleQueryRepository.findGroupCapsuleCount(groupId);

        final List<Long> groupMemberIds = groupDetailDto.members().stream()
            .map(GroupMemberDto::memberId)
            .toList();
        final List<Long> friendIds = memberFriendRepository.findFriendIds(groupMemberIds, memberId);

        return GroupDetailTotalDto.as(groupDetailDto, groupCapsuleCount, friendIds);
    }
}
