package site.timecapsulearchive.core.domain.capsule.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class GroupCapsuleOpenNotFoundException extends BusinessException {

    public GroupCapsuleOpenNotFoundException() {
        super(ErrorCode.GROUP_CAPSULE_OPEN_NOT_FOUND_ERROR);
    }
}
