package site.timecapsulearchive.core.global.error.exception;

import org.springframework.security.core.AuthenticationException;
import site.timecapsulearchive.core.global.error.ErrorCode;

/**
 * 유효하지 않은 jwt 토큰일 때 발생하는 예외
 */
public class InvalidTokenException extends AuthenticationException {

    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN_EXCEPTION.getMessage());
    }

    public InvalidTokenException(Throwable throwable) {
        super(ErrorCode.INVALID_TOKEN_EXCEPTION.getMessage(), throwable);
    }
}