package site.timecapsulearchive.core.global.error.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;

public class InternalServerException extends RuntimeException {

    public InternalServerException(Throwable e) {
        super(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), e);
    }
}
