package site.timecapsulearchive.core.global.error.exception;

public class NullCheckValidateException extends RuntimeException {

    public NullCheckValidateException(String missingField) {
        super(missingField);
    }
}
