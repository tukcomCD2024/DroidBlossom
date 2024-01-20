package site.timecapsulearchive.core.domain.auth.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class NotFoundCertificationNumberException extends BusinessException {

    public NotFoundCertificationNumberException() {
        super(ErrorCode.NOT_FOUND_CERTIFICATION_NUMBER_ERROR);
    }
}
