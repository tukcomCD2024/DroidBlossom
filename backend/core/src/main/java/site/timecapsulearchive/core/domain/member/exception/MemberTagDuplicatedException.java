package site.timecapsulearchive.core.domain.member.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class MemberTagDuplicatedException extends BusinessException {

    public MemberTagDuplicatedException() {
        super(ErrorCode.MEMBER_TAG_DUPLICATED_ERROR);
    }
}
