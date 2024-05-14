package site.timecapsulearchive.core.global.error;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.ConstraintViolation;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Schema(description = "에러 발생 시 응답")
public record ErrorResponse(
    @Schema(description = "에러 코드")
    String code,

    @Schema(description = "에러 메시지")
    String message,

    @Schema(description = "에러 리스트 ex) 필드 에러들")
    List<Error> result
) {

    public static ErrorResponse fromErrorCode(final ErrorCode errorCode) {
        return new ErrorResponse(
            errorCode.getCode(),
            errorCode.getMessage(),
            Collections.emptyList()
        );
    }

    public static ErrorResponse ofBindingResult(final ErrorCode errorCode,
        final BindingResult bindingResult) {
        return new ErrorResponse(
            errorCode.getCode(),
            errorCode.getMessage(),
            Error.fromBindingResult(bindingResult)
        );
    }

    public static ErrorResponse fromParameter(
        final ErrorCode errorCode,
        final String parameterName
    ) {
        return new ErrorResponse(
            errorCode.getCode(),
            errorCode.getMessage(),
            List.of(Error.fromParameter(parameterName))
        );
    }

    public static ErrorResponse fromType(
        final ErrorCode errorCode,
        final String parameterName,
        final String value
    ) {
        return new ErrorResponse(
            errorCode.getCode(),
            errorCode.getMessage(),
            List.of(Error.fromType(parameterName, value))
        );
    }

    public static ErrorResponse ofConstraints(
        ErrorCode errorCode,
        Set<ConstraintViolation<?>> constraintViolations
    ) {
        List<Error> errors = constraintViolations.stream()
            .map(e -> Error.of(e.getInvalidValue(), e.getMessage()))
            .toList();

        return new ErrorResponse(
            errorCode.getCode(),
            errorCode.getMessage(),
            errors
        );
    }

    public record Error(
        String field,
        String value,
        String reason
    ) {

        public static Error of(final Object value, final String reason) {
            return new Error("", String.valueOf(value), reason);
        }

        public static Error fromType(final String parameterName, final String value) {
            return new Error(parameterName, value, "입력 파라미터의 타입이 올바르지 않습니다.");
        }

        public static Error fromParameter(final String parameterName) {
            return new Error(parameterName, "", "필수 입력 파라미터를 포함하지 않았습니다.");
        }

        public static List<Error> fromBindingResult(final BindingResult bindingResult) {
            return bindingResult.getFieldErrors().stream()
                .map(Error::fromFieldError)
                .toList();
        }

        private static Error fromFieldError(final FieldError fieldError) {
            return new Error(
                fieldError.getField(),
                String.valueOf(fieldError.getRejectedValue()),
                fieldError.getDefaultMessage()
            );
        }

    }
}
