package site.timecapsulearchive.core.domain.capsuleskin.data.dto;

import lombok.Builder;

@Builder
public record CapsuleSkinMessageDto (

    Long memberId,
    String memberName,
    String skinName,
    String imageUrl,
    String motionName
){

}
