package site.timecapsulearchive.core.domain.group.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class GroupNotFoundException extends BusinessException {

    public GroupNotFoundException() {
        super(ErrorCode.GROUP_NOT_FOUND_ERROR);
    }
}
