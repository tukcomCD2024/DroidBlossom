package site.timecapsulearchive.core.domain.auth.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class NotMatchCertificationNumberException extends BusinessException {

    public NotMatchCertificationNumberException() {
        super(ErrorCode.NOT_MATCH_CERTIFICATION_NUMBER_ERROR);
    }
}
