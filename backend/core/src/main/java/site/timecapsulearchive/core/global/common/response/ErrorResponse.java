package site.timecapsulearchive.core.global.common.response;

import java.util.Collections;
import java.util.List;

public record ErrorResponse(
    String code,
    String message,
    List<Error> errors
) {

    public static ErrorResponse create(String code, String message) {
        return new ErrorResponse(code, message, Collections.emptyList());
    }

    private record Error(
        String field,
        String value,
        String reason
    ) {

    }
}
