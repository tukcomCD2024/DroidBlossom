package site.timecapsulearchive.core.domain.member.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class MemberUpdateDataException extends BusinessException {

    public MemberUpdateDataException() {
        super(ErrorCode.MEMBER_UPDATE_DATA_ERROR);
    }
}
