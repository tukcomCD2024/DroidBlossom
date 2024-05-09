package site.timecapsulearchive.core.domain.group.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class GroupMemberNotfoundException extends BusinessException {

    public GroupMemberNotfoundException() {
        super(ErrorCode.GROUP_MEMBER_NOT_FOUND_ERROR);
    }
}
