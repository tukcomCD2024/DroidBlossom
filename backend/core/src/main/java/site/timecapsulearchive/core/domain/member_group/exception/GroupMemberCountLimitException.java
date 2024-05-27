package site.timecapsulearchive.core.domain.member_group.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class GroupMemberCountLimitException extends BusinessException {

    public GroupMemberCountLimitException() {
        super(ErrorCode.GROUP_MEMBER_COUNT_LIMIT_ERROR);
    }
}