package site.timecapsulearchive.notification.infra.exception;

public class MessageNotSendableException extends RuntimeException {

    public MessageNotSendableException(String message) {
        super(message);
    }
}
