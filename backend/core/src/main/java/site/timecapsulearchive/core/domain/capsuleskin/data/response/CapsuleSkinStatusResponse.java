package site.timecapsulearchive.core.domain.capsuleskin.data.response;

import org.springframework.http.HttpStatus;

public record CapsuleSkinStatusResponse(
    HttpStatus status,
    String result
) {

    public static CapsuleSkinStatusResponse success() {
        return new CapsuleSkinStatusResponse(HttpStatus.OK ,"캡슐 스킨 생성 성공!");
    }

    public static CapsuleSkinStatusResponse sendMessage() {
        return new CapsuleSkinStatusResponse(HttpStatus.ACCEPTED, "캡슐 스킨 메세지 전송 성공!");
    }
}
