package site.timecapsulearchive.core.domain.capsuleskin.data.response;

public record CapsuleSkinStatusResponse(
    String result
) {

    public static CapsuleSkinStatusResponse success() {
        return new CapsuleSkinStatusResponse("캡슐 스킨 생성 성공!");
    }

    public static CapsuleSkinStatusResponse sendMessage() {
        return new CapsuleSkinStatusResponse("캡슐 스킨 메세지 전송 성공!");
    }
}
