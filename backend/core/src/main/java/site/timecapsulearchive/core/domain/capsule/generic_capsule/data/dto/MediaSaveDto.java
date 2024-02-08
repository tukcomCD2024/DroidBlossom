package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto;

import java.util.List;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.member.entity.Member;

public record MediaSaveDto(
    Capsule capsule,
    Member member,
    String directory,
    List<String> fileNames
) {

    public static MediaSaveDto of(
        Capsule capsule,
        Member member,
        String directory,
        List<String> fileNames
    ) {
        return new MediaSaveDto(capsule, member, directory, fileNames);
    }
}
