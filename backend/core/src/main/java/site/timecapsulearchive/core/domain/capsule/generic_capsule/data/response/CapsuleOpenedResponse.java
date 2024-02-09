package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response;

public record CapsuleOpenedResponse(
    String result
) {

    public static CapsuleOpenedResponse notOpened() {
        return new CapsuleOpenedResponse("캡슐을 열 수 없습니다.");
    }

    public static CapsuleOpenedResponse opened() {
        return new CapsuleOpenedResponse("캡슐을 열었습니다.");
    }
}
