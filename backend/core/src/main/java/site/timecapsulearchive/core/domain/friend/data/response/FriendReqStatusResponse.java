package site.timecapsulearchive.core.domain.friend.data.response;

import org.springframework.http.HttpStatus;

public record FriendReqStatusResponse(
    HttpStatus httpStatus,
    String result
) {

    public static FriendReqStatusResponse success() {
        return new FriendReqStatusResponse(HttpStatus.ACCEPTED, "친구 요청 메시지 전송 성공!");
    }
}
