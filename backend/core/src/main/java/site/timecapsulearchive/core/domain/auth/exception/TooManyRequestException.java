package site.timecapsulearchive.core.domain.auth.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class TooManyRequestException extends BusinessException {

    public TooManyRequestException() {
        super(ErrorCode.TOO_MANY_REQUEST_ERROR);
    }
}
