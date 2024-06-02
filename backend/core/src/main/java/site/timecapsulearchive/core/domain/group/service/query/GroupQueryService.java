package site.timecapsulearchive.core.domain.group.service.query;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.friend.repository.member_friend.MemberFriendRepository;
import site.timecapsulearchive.core.domain.group.data.dto.CompleteGroupSummaryDto;
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
    private final GroupCapsuleQueryRepository groupCapsuleQueryRepository;
    private final MemberFriendRepository memberFriendRepository;

    public Group findGroupById(final Long groupId) {
        return groupRepository.findGroupById(groupId)
            .orElseThrow(GroupNotFoundException::new);
    }

    public Slice<CompleteGroupSummaryDto> findGroupsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        final Slice<GroupSummaryDto> groupSummaryDtos = groupRepository.findGroupSummaries(memberId,
            size, createdAt);
        final List<Long> groupIds = groupSummaryDtos.getContent().stream().map(GroupSummaryDto::id)
            .toList();

        final List<String> groupOwnerProfileUrls = groupRepository.getGroupOwnerProfileUrls(
            groupIds);
        final List<Long> totalGroupMemberCount = groupRepository.getTotalGroupMemberCount(groupIds);

        final List<CompleteGroupSummaryDto> finalGroupSummaryDtos = IntStream.range(0,
                groupSummaryDtos.getContent().size())
            .mapToObj(i -> new CompleteGroupSummaryDto(
                    groupSummaryDtos.getContent().get(i),
                    groupOwnerProfileUrls.get(i),
                    totalGroupMemberCount.get(i)
                )
            )
            .toList();

        return new SliceImpl<>(finalGroupSummaryDtos, groupSummaryDtos.getPageable(),
            groupSummaryDtos.hasNext());
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

    public List<GroupMemberDto> findGroupMemberInfos(
        final Long memberId,
        final Long groupId
    ) {
        return groupRepository.findGroupMemberInfos(memberId, groupId);
    }
}
