package site.timecapsulearchive.notification.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.timecapsulearchive.notification.infra.exception.MessageNotSendAbleException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleGlobalException(final Exception e) {
        log.error(e.getMessage(), e);
        final ErrorResponse errorResponse = ErrorResponse.exception();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse);
    }

    @ExceptionHandler(MessageNotSendAbleException.class)
    protected ResponseEntity<ErrorResponse> handleMessageNotSendableException(
        final MessageNotSendAbleException e) {
        log.warn(e.getMessage(), e);

        final ErrorResponse errorResponse = ErrorResponse.messageNotSendable();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleRequestArgumentNotValidException(
        MethodArgumentNotValidException e
    ) {
        log.warn(e.getMessage(), e);

        final ErrorResponse errorResponse = ErrorResponse.methodArgumentNotValid();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleRequestTypeNotValidException(
        HttpMessageNotReadableException e
    ) {
        log.warn(e.getMessage(), e);

        final ErrorResponse response = ErrorResponse.httpMessageNotReadable();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
}
