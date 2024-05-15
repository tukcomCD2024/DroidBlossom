package site.timecapsulearchive.core.domain.member_group.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class MemberGroupNotFoundException extends BusinessException {

    public MemberGroupNotFoundException() {
        super(ErrorCode.GROUP_MEMBER_NOT_FOUND_ERROR);
    }
}