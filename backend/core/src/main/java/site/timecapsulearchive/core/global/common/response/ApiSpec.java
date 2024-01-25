package site.timecapsulearchive.core.global.common.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "api 공통 응답 ")
public record ApiSpec<T>(

    @Schema(description = "응답 코드")
    String code,

    @Schema(description = "응답 메시지")
    String message,

    @Schema(description = "응답 데이터")
    T result
) {

    public static <T> ApiSpec<T> empty(SuccessCode code) {
        return new ApiSpec<>(code.getMessage(), code.getCode(), null);
    }

    public static <T> ApiSpec<T> success(SuccessCode code, T result) {
        return new ApiSpec<>(code.getCode(), code.getMessage(), result);
    }
}
