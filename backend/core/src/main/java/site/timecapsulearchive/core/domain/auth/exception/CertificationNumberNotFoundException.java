package site.timecapsulearchive.core.domain.auth.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class CertificationNumberNotFoundException extends BusinessException {

    public CertificationNumberNotFoundException() {
        super(ErrorCode.CERTIFICATION_NUMBER_NOT_FOUND_ERROR);
    }
}
