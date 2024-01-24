package site.timecapsulearchive.core.domain.member.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class AlreadyVerifiedException extends BusinessException {

    public AlreadyVerifiedException() {
        super(ErrorCode.ALREADY_VERIFIED_ERROR);
    }
}
