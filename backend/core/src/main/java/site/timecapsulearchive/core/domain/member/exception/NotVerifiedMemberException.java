package site.timecapsulearchive.core.domain.member.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class NotVerifiedMemberException extends BusinessException {

    public NotVerifiedMemberException() {
        super(ErrorCode.LOGIN_ON_NOT_VERIFIED_ERROR);
    }
}
