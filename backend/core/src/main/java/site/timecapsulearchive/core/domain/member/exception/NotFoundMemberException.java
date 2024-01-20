package site.timecapsulearchive.core.domain.member.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class NotFoundMemberException extends BusinessException {

    public NotFoundMemberException() {
        super(ErrorCode.NOT_FOUND_MEMBER_ERROR);
    }
}
