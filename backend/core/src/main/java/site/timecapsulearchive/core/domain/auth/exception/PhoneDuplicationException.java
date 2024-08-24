package site.timecapsulearchive.core.domain.auth.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class PhoneDuplicationException extends BusinessException {

    public PhoneDuplicationException() {
        super(ErrorCode.PHONE_DUPLICATION_ERROR);
    }
}
