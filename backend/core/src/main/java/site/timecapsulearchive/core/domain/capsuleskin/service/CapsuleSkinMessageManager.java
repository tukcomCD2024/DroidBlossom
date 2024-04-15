package site.timecapsulearchive.core.domain.capsuleskin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinCreateDto;
import site.timecapsulearchive.core.domain.capsuleskin.data.mapper.CapsuleSkinMapper;
import site.timecapsulearchive.core.global.config.rabbitmq.RabbitmqComponentConstants;

@Component
@RequiredArgsConstructor
public class CapsuleSkinMessageManager {

    private final RabbitTemplate rabbitTemplate;
    private final CapsuleSkinMapper capsuleSkinMapper;

    public void sendSkinCreateMessage(
        final Long memberId,
        final String nickname,
        final CapsuleSkinCreateDto dto
    ) {
        rabbitTemplate.convertAndSend(
            RabbitmqComponentConstants.CAPSULE_SKIN_EXCHANGE.getValue(),
            "",
            capsuleSkinMapper.createDtoToMessageDto(memberId, nickname, dto)
        );
    }
}
