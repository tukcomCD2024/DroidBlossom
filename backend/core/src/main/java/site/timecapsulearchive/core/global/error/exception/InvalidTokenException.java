package site.timecapsulearchive.core.global.error.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;

/**
 * 유효하지 않은 jwt 토큰일 때 발생하는 예외
 */
public class InvalidTokenException extends BusinessException {

    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN_ERROR);
    }

    public InvalidTokenException(Throwable throwable) {
        super(ErrorCode.INVALID_TOKEN_ERROR, throwable);
    }
}