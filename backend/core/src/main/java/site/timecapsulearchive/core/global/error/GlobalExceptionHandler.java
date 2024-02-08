package site.timecapsulearchive.core.global.error;

import static site.timecapsulearchive.core.global.error.ErrorCode.INPUT_INVALID_TYPE_ERROR;
import static site.timecapsulearchive.core.global.error.ErrorCode.INPUT_INVALID_VALUE_ERROR;
import static site.timecapsulearchive.core.global.error.ErrorCode.INTERNAL_SERVER_ERROR;
import static site.timecapsulearchive.core.global.error.ErrorCode.REQUEST_PARAMETER_NOT_FOUND_ERROR;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.timecapsulearchive.core.global.error.exception.BusinessException;
import site.timecapsulearchive.core.infra.sms.exception.ExternalApiException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleGlobalException(final Exception e) {
        log.error(e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.create(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR.getStatus())
            .body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        log.error(e.getMessage(), e);

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.create(errorCode);

        return ResponseEntity.status(errorCode.getStatus())
            .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleRequestArgumentNotValidException(
        MethodArgumentNotValidException e
    ) {
        log.warn(e.getMessage(), e);

        ErrorResponse response = ErrorResponse.create(INPUT_INVALID_VALUE_ERROR,
            e.getBindingResult());
        return ResponseEntity.status(INPUT_INVALID_VALUE_ERROR.getStatus())
            .body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleRequestTypeNotValidException(
        HttpMessageNotReadableException e
    ) {
        log.warn(e.getMessage(), e);

        ErrorResponse response = ErrorResponse.create(INPUT_INVALID_TYPE_ERROR);
        return ResponseEntity.status(INPUT_INVALID_VALUE_ERROR.getStatus())
            .body(response);
    }

    @ExceptionHandler(ExternalApiException.class)
    protected ResponseEntity<ErrorResponse> handleExternalApiException(ExternalApiException e) {
        log.warn(e.getMessage(), e);

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.create(errorCode);

        return ResponseEntity.status(errorCode.getStatus())
            .body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
        MissingServletRequestParameterException e
    ) {
        log.warn(e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.parameter(REQUEST_PARAMETER_NOT_FOUND_ERROR,
            e.getParameterName());

        return ResponseEntity.status(REQUEST_PARAMETER_NOT_FOUND_ERROR.getStatus())
            .body(errorResponse);
    }
}
