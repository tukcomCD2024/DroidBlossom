package site.timecapsulearchive.core.global.error;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Collections;
import java.util.List;
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

    public static ErrorResponse create(final ErrorCode errorCode) {
        return new ErrorResponse(
            errorCode.getCode(),
            errorCode.getMessage(),
            Collections.emptyList()
        );
    }

    public static ErrorResponse create(final ErrorCode errorCode,
        final BindingResult bindingResult) {
        return new ErrorResponse(
            errorCode.getCode(),
            errorCode.getMessage(),
            Error.from(bindingResult)
        );
    }

    public static ErrorResponse parameter(
        final ErrorCode errorCode,
        final String parameterName
    ) {
        return new ErrorResponse(
            errorCode.getCode(),
            errorCode.getMessage(),
            List.of(Error.parameter(parameterName))
        );
    }

    public record Error(
        String field,
        String value,
        String reason
    ) {

        public static Error parameter(String parameterName) {
            return new Error(parameterName, null, "필수 입력 파라미터를 포함하지 않았습니다.");
        }

        public static List<Error> from(final BindingResult bindingResult) {
            return bindingResult.getFieldErrors().stream()
                .map(Error::from)
                .toList();
        }

        private static Error from(final FieldError fieldError) {
            return new Error(
                fieldError.getField(),
                String.valueOf(fieldError.getRejectedValue()),
                fieldError.getDefaultMessage()
            );
        }

    }
}
