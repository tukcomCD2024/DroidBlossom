package site.timecapsulearchive.core.domain.group.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class GroupMemberDuplicatedIdException extends BusinessException {

    public GroupMemberDuplicatedIdException() {
        super(ErrorCode.GROUP_MEMBER_DUPLICATED_ID_ERROR);
    }
}