package site.timecapsulearchive.core.domain.auth.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class CertificationNumberNotMatchException extends BusinessException {

    public CertificationNumberNotMatchException() {
        super(ErrorCode.CERTIFICATION_NUMBER_NOT_MATCH_ERROR);
    }
}
