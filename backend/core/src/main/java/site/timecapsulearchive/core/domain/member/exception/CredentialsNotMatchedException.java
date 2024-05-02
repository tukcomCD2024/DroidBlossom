package site.timecapsulearchive.core.domain.member.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class CredentialsNotMatchedException extends BusinessException {

    public CredentialsNotMatchedException() {
        super(ErrorCode.CREDENTIALS_NOT_MATCHED_ERROR);
    }
}
