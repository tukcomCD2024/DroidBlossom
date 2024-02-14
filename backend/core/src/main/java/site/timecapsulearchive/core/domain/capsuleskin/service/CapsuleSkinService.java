package site.timecapsulearchive.core.domain.capsuleskin.service;


import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinCreateDto;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinSummaryDto;
import site.timecapsulearchive.core.domain.capsuleskin.data.mapper.CapsuleSkinMapper;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinStatusResponse;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinsSliceResponse;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.capsuleskin.repository.CapsuleSkinQueryRepository;
import site.timecapsulearchive.core.domain.capsuleskin.repository.CapsuleSkinRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.global.config.RabbitmqConfig;

@Service
@RequiredArgsConstructor
public class CapsuleSkinService {

    private final CapsuleSkinRepository capsuleSkinRepository;
    private final CapsuleSkinQueryRepository capsuleSkinQueryRepository;
    private final MemberRepository memberRepository;
    private final CapsuleSkinMapper capsuleSkinMapper;
    private final RabbitmqConfig rabbitmqConfig;
    private final RabbitTemplate rabbitTemplate;

    public CapsuleSkinsSliceResponse findCapsuleSkinSliceByCreatedAtAndMemberId(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        final Slice<CapsuleSkinSummaryDto> slice = capsuleSkinQueryRepository.findCapsuleSkinSliceByCreatedAtAndMemberId(
            memberId,
            size,
            createdAt
        );

        return capsuleSkinMapper.capsuleSkinSliceToResponse(slice.getContent(), slice.hasNext());
    }

    public CapsuleSkinStatusResponse sendCapsuleSkinCreateMessage(
        final Long memberId,
        final CapsuleSkinCreateDto dto
    ) {
        Member foundMember = memberRepository.findMemberById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        if (isNotExistMotionNameAndRetarget(dto)) {
            CapsuleSkin capsuleSkin = capsuleSkinMapper.createDtoToEntity(dto, foundMember);

            capsuleSkinRepository.save(capsuleSkin);
            return CapsuleSkinStatusResponse.success();
        }

        rabbitTemplate.convertAndSend(
            rabbitmqConfig.getCapsuleSkinExchangeName(),
            rabbitmqConfig.getRoutingKey(),
            capsuleSkinMapper.createDtoToMessageDto(memberId, foundMember.getNickname(), dto)
        );
        return CapsuleSkinStatusResponse.sendMessage();
    }

    private boolean isNotExistMotionNameAndRetarget(CapsuleSkinCreateDto dto) {
        return dto.motionName() == null && dto.retarget() == null;
    }
}
