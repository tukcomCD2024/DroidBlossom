package site.timecapsulearchive.core.domain.group.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class GroupCreateException extends BusinessException {

    public GroupCreateException() {
        super(ErrorCode.GROUP_CREATE_ERROR);
    }
}
