package site.timecapsulearchive.notification.global.error;

public record ErrorResponse(String message) {

    public static ErrorResponse httpMessageNotReadable() {
        return new ErrorResponse("요청 메시지를 읽을 수 없습니다.");
    }

    public static ErrorResponse messageNotSendable() {
        return new ErrorResponse("메시지를 보낼 수 없습니다.");
    }

    public static ErrorResponse methodArgumentNotValid() {
        return new ErrorResponse("타당하지 않은 요청 인자 입니다.");
    }

    public static ErrorResponse exception() {
        return new ErrorResponse("서버에 에러가 발생하였습니다.");
    }
}
