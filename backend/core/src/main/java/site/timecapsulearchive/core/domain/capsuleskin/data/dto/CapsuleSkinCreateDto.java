package site.timecapsulearchive.core.domain.capsuleskin.data.dto;

public record CapsuleSkinCreateDto(

    String skinName,
    String imageUrl,
    String directory,
    String motionName
) {

}
