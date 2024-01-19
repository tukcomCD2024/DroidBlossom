package site.timecapsulearchive.core.infra.sms.exception;

import lombok.Getter;
import site.timecapsulearchive.core.global.error.ErrorCode;

@Getter
public class ExternalApiException extends RuntimeException {

    private final ErrorCode errorCode;

    public ExternalApiException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
