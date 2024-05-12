package site.timecapsulearchive.notification.infra.exception;

public class MessageNotSendAbleException extends RuntimeException {

    public MessageNotSendAbleException(Throwable e) {
        super("메시지를 보낼 수 없습니다.", e);
    }
}
