package site.timecapsulearchive.core.domain.capsule.dto.response;

public record CapsuleOpenedResponse(
    String answer
) {
    public static CapsuleOpenedResponse from(String answer) {
        return new CapsuleOpenedResponse(answer);
    }
}
