package site.timecapsulearchive.core.global.error.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;

public class RedisLockException extends RuntimeException {

    public RedisLockException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
