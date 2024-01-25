package site.timecapsulearchive.core.global.error.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;

public class EncryptionException extends RuntimeException {

    public EncryptionException(Throwable e) {
        super(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), e);
    }
}
