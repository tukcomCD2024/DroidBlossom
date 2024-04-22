package site.timecapsulearchive.core.domain.capsuleskin.data.dto;

import site.timecapsulearchive.core.domain.capsuleskin.entity.Motion;
import site.timecapsulearchive.core.domain.capsuleskin.entity.Retarget;

public record CapsuleSkinCreateDto(
    String skinName,
    String imageUrl,
    String directory,
    Motion motionName,
    Retarget retarget
) {

}
