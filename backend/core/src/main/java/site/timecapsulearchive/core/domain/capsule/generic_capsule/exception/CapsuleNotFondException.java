package site.timecapsulearchive.core.domain.capsule.generic_capsule.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class CapsuleNotFondException extends BusinessException {

    public CapsuleNotFondException() {
        super(ErrorCode.CAPSULE_NOT_FOUND_ERROR);
    }
}
