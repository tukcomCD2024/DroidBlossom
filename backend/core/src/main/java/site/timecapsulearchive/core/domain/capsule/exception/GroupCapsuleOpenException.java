package site.timecapsulearchive.core.domain.capsule.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class GroupCapsuleOpenException extends BusinessException {

    public GroupCapsuleOpenException() {
        super(ErrorCode.NOT_GROUP_CAPSULE_ERROR);
    }
}
