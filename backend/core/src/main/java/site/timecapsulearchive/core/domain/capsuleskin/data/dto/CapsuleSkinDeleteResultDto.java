package site.timecapsulearchive.core.domain.capsuleskin.data.dto;

public record CapsuleSkinDeleteResultDto(
    CapsuleSkinDeleteResult capsuleSkinDeleteResult,
    String message
) {

    public static CapsuleSkinDeleteResultDto fail() {
        return new CapsuleSkinDeleteResultDto(CapsuleSkinDeleteResult.FAIL,
            "캡슐 스킨이 존재하지 않거나 캡슐에 사용되고 있습니다.");
    }

    public static CapsuleSkinDeleteResultDto success() {
        return new CapsuleSkinDeleteResultDto(CapsuleSkinDeleteResult.SUCCESS, "캡슐 스킨 삭제에 성공했습니다.");
    }
}
