package site.timecapsulearchive.core.domain.capsule.dto.response;

public record CapsuleOpenedResponse(
    String result
) {

    public static CapsuleOpenedResponse notOpened() {
        return new CapsuleOpenedResponse("NO");
    }

    public static CapsuleOpenedResponse opened() {
        return new CapsuleOpenedResponse("YES");
    }
}
