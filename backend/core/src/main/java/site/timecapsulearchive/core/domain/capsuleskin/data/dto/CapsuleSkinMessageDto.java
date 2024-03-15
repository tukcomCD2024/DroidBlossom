package site.timecapsulearchive.core.domain.capsuleskin.data.dto;

import lombok.Builder;
import site.timecapsulearchive.core.domain.capsuleskin.entity.Motion;
import site.timecapsulearchive.core.domain.capsuleskin.entity.Retarget;

@Builder
public record CapsuleSkinMessageDto(

    Long memberId,
    String memberName,
    String skinName,
    String imageUrl,
    Motion motionName,
    Retarget retarget
) {

}
