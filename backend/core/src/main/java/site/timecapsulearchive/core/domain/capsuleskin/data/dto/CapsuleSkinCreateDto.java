package site.timecapsulearchive.core.domain.capsuleskin.data.dto;

import lombok.Builder;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.capsuleskin.entity.Motion;
import site.timecapsulearchive.core.domain.capsuleskin.entity.Retarget;
import site.timecapsulearchive.core.domain.member.entity.Member;

@Builder
public record CapsuleSkinCreateDto(
    String skinName,
    String imageFullPath,
    Motion motionName,
    Retarget retarget
) {

    public CapsuleSkin toCapsuleSkin(Member foundMember) {
        return CapsuleSkin.createOf(skinName, imageFullPath, foundMember);
    }
}
