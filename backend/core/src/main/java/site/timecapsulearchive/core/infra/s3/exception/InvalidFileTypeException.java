package site.timecapsulearchive.core.infra.s3.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class InvalidFileTypeException extends BusinessException {

    public InvalidFileTypeException() {
        super(ErrorCode.INPUT_INVALID_TYPE_ERROR);
    }
}
