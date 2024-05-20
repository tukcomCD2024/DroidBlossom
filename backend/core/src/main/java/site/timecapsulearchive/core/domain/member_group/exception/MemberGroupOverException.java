package site.timecapsulearchive.core.domain.member_group.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class MemberGroupOverException extends BusinessException {

    public MemberGroupOverException() {
        super(ErrorCode.GROUP_MEMBER_OVER_ERROR);
    }
}