package site.timecapsulearchive.core.domain.member_group.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class MemberGroupKickDuplicatedIdException extends BusinessException {

    public MemberGroupKickDuplicatedIdException() {
        super(ErrorCode.GROUP_MEMBER_DUPLICATED_ID_ERROR);
    }
}