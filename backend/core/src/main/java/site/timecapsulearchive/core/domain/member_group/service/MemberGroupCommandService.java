package site.timecapsulearchive.core.domain.member_group.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.exception.GroupNotFoundException;
import site.timecapsulearchive.core.domain.group.repository.GroupRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupOwnerSummaryDto;
import site.timecapsulearchive.core.domain.member_group.data.request.SendGroupRequest;
import site.timecapsulearchive.core.domain.member_group.entity.MemberGroup;
import site.timecapsulearchive.core.domain.member_group.exception.GroupInviteNotFoundException;
import site.timecapsulearchive.core.domain.member_group.exception.GroupMemberCountLimitException;
import site.timecapsulearchive.core.domain.member_group.exception.MemberGroupKickDuplicatedIdException;
import site.timecapsulearchive.core.domain.member_group.exception.MemberGroupNotFoundException;
import site.timecapsulearchive.core.domain.member_group.exception.NoGroupAuthorityException;
import site.timecapsulearchive.core.domain.member_group.repository.groupInviteRepository.GroupInviteRepository;
import site.timecapsulearchive.core.domain.member_group.repository.memberGroupRepository.MemberGroupRepository;
import site.timecapsulearchive.core.global.config.redis.RedissonLock;
import site.timecapsulearchive.core.infra.queue.manager.SocialNotificationManager;

@Service
@RequiredArgsConstructor
public class MemberGroupCommandService {

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final GroupInviteRepository groupInviteRepository;
    private final TransactionTemplate transactionTemplate;
    private final SocialNotificationManager socialNotificationManager;

    public void inviteGroup(final Long memberId, final SendGroupRequest sendGroupRequest) {
        final List<Long> friendIds = sendGroupRequest.targetIds();
        final Long groupMembersCount = memberGroupRepository.findGroupMembersCount(
            sendGroupRequest.groupId()).orElseThrow(GroupNotFoundException::new);
        if (groupMembersCount + friendIds.size() > 30) {
            throw new GroupMemberCountLimitException();
        }

        final GroupOwnerSummaryDto groupOwnerSummaryDto = transactionTemplate.execute(status -> {
            GroupOwnerSummaryDto dto = memberGroupRepository.findOwnerInMemberGroup(
                sendGroupRequest.groupId(), memberId).orElseThrow(GroupNotFoundException::new);

            if (!dto.isOwner()) {
                throw new NoGroupAuthorityException();
            }

            groupInviteRepository.bulkSave(memberId, sendGroupRequest.groupId(), friendIds);
            return dto;
        });

        socialNotificationManager.sendGroupInviteMessage(groupOwnerSummaryDto.nickname(),
            groupOwnerSummaryDto.groupProfileUrl(), friendIds);
    }

    @Transactional
    public void rejectRequestGroup(final Long memberId, final Long groupId, final Long targetId) {
        final int isDenyRequest = groupInviteRepository.deleteGroupInviteByGroupIdAndGroupOwnerIdAndGroupMemberId(
            groupId, targetId, memberId);

        if (isDenyRequest != 1) {
            throw new GroupInviteNotFoundException();
        }
    }

    @RedissonLock(value = "#groupId")
    public void acceptGroupInvite(final Long memberId, final Long groupId) {
        final Long totalGroupMemberCount = groupRepository.getTotalGroupMemberCount(groupId)
            .orElseThrow(GroupNotFoundException::new);

        if (totalGroupMemberCount == 30) {
            throw new GroupMemberCountLimitException();
        }

        final Member groupMember = memberRepository.findMemberById(memberId)
            .orElseThrow(MemberNotFoundException::new);
        final Group group = groupRepository.findGroupById(groupId)
            .orElseThrow(GroupNotFoundException::new);
        final Long groupOwnerId = memberGroupRepository.findGroupOwnerId(groupId)
            .orElseThrow(GroupNotFoundException::new);

        transactionTemplate.executeWithoutResult(status -> {
            final int isDenyRequest = groupInviteRepository.deleteGroupInviteByGroupIdAndGroupOwnerIdAndGroupMemberId(
                groupId, groupOwnerId, memberId);
            if (isDenyRequest != 1) {
                throw new GroupInviteNotFoundException();
            }

            memberGroupRepository.save(MemberGroup.createGroupMember(groupMember, group));
        });

        socialNotificationManager.sendGroupAcceptMessage(groupMember.getNickname(), groupOwnerId);
    }

    /**
     * 사용자는 사용자가 속한 그룹을 탈퇴한다.
     * <br><u><b>주의</b></u> - 그룹 탈퇴 시 아래 조건에 해당하면 예외가 발생한다.
     * <br>1. 그룹원이 그룹장인 경우
     *
     * @param memberId 그룹에서 탈퇴할 사용자
     * @param groupId  탈퇴할 그룹 아이디
     */
    @Transactional
    public void quitGroup(final Long memberId, final Long groupId) {
        final MemberGroup groupMember = memberGroupRepository.findMemberGroupByMemberIdAndGroupId(
            memberId, groupId).orElseThrow(MemberGroupNotFoundException::new);
        groupMember.checkGroupMemberOwner();

        memberGroupRepository.delete(groupMember);
    }

    /**
     * 그룹의 회원을 그룹에서 삭제한다.
     * <br><u><b>주의</b></u> - 그룹원 삭제 시 아래 조건에 해당하면 예외가 발생한다.
     * <br>1. 자신을 삭제하려한 경우
     * <br>2. 삭제를 요청한 사용자가 그룹장이 아닌 경우
     * <br>3. 삭제할 대상이 없는 경우
     *
     * @param groupOwnerId  그룹장 여부 조회할 대상 사용자
     * @param groupId       해당 그룹 아이디
     * @param groupMemberId 삭제할 그룹원 아이디
     */
    @Transactional
    public void kickGroupMember(
        final Long groupOwnerId,
        final Long groupId,
        final Long groupMemberId
    ) {
        if (groupOwnerId.equals(groupMemberId)) {
            throw new MemberGroupKickDuplicatedIdException();
        }

        checkGroupOwnership(groupOwnerId, groupId);

        final MemberGroup memberGroup = memberGroupRepository.findMemberGroupByMemberIdAndGroupId(
                groupMemberId, groupId)
            .orElseThrow(MemberGroupNotFoundException::new);

        memberGroupRepository.delete(memberGroup);
    }

    private void checkGroupOwnership(Long groupOwnerId, Long groupId) {
        final Boolean isOwner = memberGroupRepository.findIsOwnerByMemberIdAndGroupId(
            groupOwnerId, groupId).orElseThrow(MemberGroupNotFoundException::new);

        if (!isOwner) {
            throw new NoGroupAuthorityException();
        }
    }
}
