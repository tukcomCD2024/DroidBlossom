package site.timecapsulearchive.core.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        log.error("handleBusinessException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse errorResponse = ErrorResponse.create(errorCode.getCode(),
            errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatus())
            .body(errorResponse);
    }
}
