package site.timecapsulearchive.core.global.error.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;

public class AESEncryptionException extends RuntimeException {
    public AESEncryptionException(Throwable e) {
        super(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), e);
    }
}
