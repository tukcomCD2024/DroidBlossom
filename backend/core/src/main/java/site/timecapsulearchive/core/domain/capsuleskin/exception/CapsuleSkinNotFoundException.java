package site.timecapsulearchive.core.domain.capsuleskin.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class CapsuleSkinNotFoundException extends BusinessException {

    public CapsuleSkinNotFoundException() {
        super(ErrorCode.CAPSULE_SKIN_NOT_FOUND_ERROR);
    }
}
