package site.timecapsulearchive.core.domain.member_group.repository.group_invite_repository;

import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.domain.Slice;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupInviteSummaryDto;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupSendingInviteMemberDto;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupSendingInvitesSliceRequestDto;

public interface GroupInviteQueryRepository {

    void bulkSave(final Long groupOwnerId, final Long groupId, final List<Long> groupMemberIds);

    List<Long> findGroupInviteIdsByGroupIdAndGroupOwnerId(final Long groupId, final Long memberId);

    Slice<GroupInviteSummaryDto> findGroupReceivingInvitesSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    );
  
    Slice<GroupSendingInviteMemberDto> findGroupSendingInvites(
        final GroupSendingInvitesSliceRequestDto dto
    );
}
