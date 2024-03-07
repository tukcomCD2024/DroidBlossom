package site.timecapsulearchive.notification.infra.exception;

public class MessageNotSendableException extends RuntimeException {

    public MessageNotSendableException(Throwable e, String code) {
        super(code + "메시지를 보낼 수 없습니다.", e);
    }
}
