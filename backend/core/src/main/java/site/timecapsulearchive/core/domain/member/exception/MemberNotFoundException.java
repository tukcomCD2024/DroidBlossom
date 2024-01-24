package site.timecapsulearchive.core.domain.member.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class MemberNotFoundException extends BusinessException {

    public MemberNotFoundException() {
        super(ErrorCode.NOT_FOUND_MEMBER_ERROR);
    }
}
