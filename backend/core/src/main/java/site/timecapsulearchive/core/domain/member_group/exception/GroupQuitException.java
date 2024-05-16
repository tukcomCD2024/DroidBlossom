package site.timecapsulearchive.core.domain.member_group.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class GroupQuitException extends BusinessException {

    public GroupQuitException(ErrorCode errorCode) {
        super(errorCode);
    }
}