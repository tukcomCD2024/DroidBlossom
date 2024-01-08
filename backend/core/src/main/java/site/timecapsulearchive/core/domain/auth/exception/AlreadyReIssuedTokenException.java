package site.timecapsulearchive.core.domain.auth.exception;

import site.timecapsulearchive.core.global.common.response.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

/**
 * 이미 액세스 토큰 재발급에 사용된 리프레시 토큰일 때 발생하는 예외
 */
public class AlreadyReIssuedTokenException extends BusinessException {

    public AlreadyReIssuedTokenException() {
        super(ErrorCode.ALREADY_RE_ISSUED_TOKEN_EXCEPTION);
    }
}
