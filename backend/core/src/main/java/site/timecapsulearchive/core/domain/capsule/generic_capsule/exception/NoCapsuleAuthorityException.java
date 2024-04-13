package site.timecapsulearchive.core.domain.capsule.generic_capsule.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class NoCapsuleAuthorityException extends BusinessException {

    public NoCapsuleAuthorityException() {
        super(ErrorCode.NO_CAPSULE_AUTHORITY_ERROR);
    }
}
