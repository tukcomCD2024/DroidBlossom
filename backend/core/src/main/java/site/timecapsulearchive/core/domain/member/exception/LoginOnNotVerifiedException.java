package site.timecapsulearchive.core.domain.member.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class LoginOnNotVerifiedException extends BusinessException {

    public LoginOnNotVerifiedException() {
        super(ErrorCode.LOGIN_ON_NOT_VERIFIED_ERROR);
    }
}
