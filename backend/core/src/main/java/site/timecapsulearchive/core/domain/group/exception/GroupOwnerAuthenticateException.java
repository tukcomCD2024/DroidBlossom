package site.timecapsulearchive.core.domain.group.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class GroupOwnerAuthenticateException extends BusinessException {

    public GroupOwnerAuthenticateException() {
        super(ErrorCode.GROUP_OWNER_AUTHENTICATE_ERROR);
    }
}
