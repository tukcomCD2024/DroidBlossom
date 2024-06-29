package site.timecapsulearchive.core.domain.member.facade;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.auth.service.TokenManager;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.service.CapsuleService;
import site.timecapsulearchive.core.domain.capsule.group_capsule.service.GroupCapsuleOpenService;
import site.timecapsulearchive.core.domain.capsule.group_capsule.service.GroupCapsuleService;
import site.timecapsulearchive.core.domain.friend.service.command.FriendCommandService;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.service.command.GroupCommandService;
import site.timecapsulearchive.core.domain.group.service.query.GroupQueryService;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.service.MemberService;
import site.timecapsulearchive.core.global.common.supplier.ZonedDateTimeSupplier;

@Component
@RequiredArgsConstructor
public class MemberFacade {

    private final TokenManager tokenManager;
    private final MemberService memberService;
    private final CapsuleService capsuleService;
    private final FriendCommandService friendCommandService;
    private final GroupCommandService groupCommandService;
    private final GroupCapsuleOpenService groupCapsuleOpenService;
    private final GroupQueryService groupQueryService;
    private final GroupCapsuleService groupCapsuleService;

    @Transactional
    public void deleteByMemberId(final Long memberId, final String accessToken) {
        tokenManager.removeRefreshToken(memberId);

        tokenManager.addBlackList(memberId, accessToken);

        final Member member = memberService.findMemberById(memberId);

        final ZonedDateTime deletedAt = ZonedDateTimeSupplier.utc().get();
        groupCapsuleOpenService.deleteAllByMemberId(memberId, deletedAt);
        capsuleService.deleteRelatedAllByMemberId(memberId, deletedAt);

        friendCommandService.deleteRelatedAllByMemberId(memberId, deletedAt);

        memberService.deleteNotificationByMemberId(memberId, deletedAt);

        groupCommandService.deleteRelatedAllByMemberId(memberId, deletedAt);

        final List<Group> allOwnerGroups = groupQueryService.findAllOwnerGroupsByMemberId(
            memberId);
        groupCapsuleService.deleteRelatedAllOwnerGroup(memberId, allOwnerGroups, deletedAt);
        groupCommandService.deleteRelatedAllOwnerGroup(allOwnerGroups, deletedAt);

        memberService.delete(member);
    }
}