package site.timecapsulearchive.core.domain.member_group.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class NoGroupAuthorityException extends BusinessException {

    public NoGroupAuthorityException() {
        super(ErrorCode.NO_GROUP_AUTHORITY_ERROR);
    }
}