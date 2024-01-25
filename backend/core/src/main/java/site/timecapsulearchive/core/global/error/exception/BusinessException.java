package site.timecapsulearchive.core.global.error.exception;

import lombok.Getter;
import site.timecapsulearchive.core.global.error.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());

        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode.getMessage(), throwable);

        this.errorCode = errorCode;
    }
}
