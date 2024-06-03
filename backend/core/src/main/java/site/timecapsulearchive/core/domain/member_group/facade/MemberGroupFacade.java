package site.timecapsulearchive.core.domain.member_group.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupAcceptNotificationDto;
import site.timecapsulearchive.core.domain.member_group.service.MemberGroupCommandService;
import site.timecapsulearchive.core.infra.queue.manager.SocialNotificationManager;

@Component
@RequiredArgsConstructor
public class MemberGroupFacade {

    private final MemberGroupCommandService memberGroupCommandService;
    private final SocialNotificationManager socialNotificationManager;

    public void acceptGroupInvite(final Long memberId, final Long groupId) {
        final GroupAcceptNotificationDto groupAcceptNotificationDto = memberGroupCommandService.acceptGroupInvite(
            memberId, groupId);
        socialNotificationManager.sendGroupAcceptMessage(
            groupAcceptNotificationDto.groupMemberNickname(),
            groupAcceptNotificationDto.groupOwnerId())
        ;
    }

}
