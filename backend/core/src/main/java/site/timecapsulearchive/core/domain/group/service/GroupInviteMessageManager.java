package site.timecapsulearchive.core.domain.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.group.data.dto.GroupInviteMessageDto;
import site.timecapsulearchive.core.global.config.rabbitmq.RabbitmqComponentConstants;

@Component
@RequiredArgsConstructor
public class GroupInviteMessageManager {

    private final RabbitTemplate rabbitTemplate;

    public void sendGroupInviteMessage(
        final GroupInviteMessageDto groupInviteMessageDto
    ) {
        rabbitTemplate.convertAndSend(
            RabbitmqComponentConstants.GROUP_INVITE_EXCHANGE_NAME.getValue(),
            RabbitmqComponentConstants.GROUP_INVITE_QUEUE_NAME.getValue(),
            groupInviteMessageDto
        );
    }
}
