package site.timecapsulearchive.core.global.error;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Collections;
import java.util.List;

@Schema(description = "에러 발생 시 응답")
public record ErrorResponse(

    @Schema(description = "에러 코드")
    String code,

    @Schema(description = "에러 메시지")
    String message,

    @Schema(description = "에러 리스트 ex) 필드 에러들")
    List<Error> result
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
