package site.timecapsulearchive.core.domain.group.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class GroupDeleteFailException extends BusinessException {

    public GroupDeleteFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
