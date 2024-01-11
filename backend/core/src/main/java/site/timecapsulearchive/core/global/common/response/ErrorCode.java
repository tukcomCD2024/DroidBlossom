package site.timecapsulearchive.core.global.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_TOKEN_EXCEPTION("A001", "jwt 토큰이 유효하지 않습니다."),
    ALREADY_RE_ISSUED_TOKEN_EXCEPTION("A002", "이미 액세스 토큰 재발급에 사용된 리프레시 토큰입니다.");

    private final String code;
    private final String message;
}
