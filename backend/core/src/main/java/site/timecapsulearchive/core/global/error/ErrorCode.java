package site.timecapsulearchive.core.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //global
    INTERNAL_SERVER_ERROR(500, "G001", "서버에 오류가 발생하였습니다."),

    //jwt
    INVALID_TOKEN_EXCEPTION(400, "J001", "jwt 토큰이 유효하지 않습니다."),
    ALREADY_RE_ISSUED_TOKEN_EXCEPTION(400, "J002", "이미 액세스 토큰 재발급에 사용된 리프레시 토큰입니다."),

    //auth
    AUTHENTICATION_EXCEPTION(401, "A001", "인증에 실패했습니다. 인증 수단이 유효한지 확인하세요."),

    //ouath
    OAUTH2_NOT_AUTHENTICATED_EXCEPTION(401, "O001", "OAuth2 인증에 실패하였습니다.");

    private final int status;
    private final String code;
    private final String message;
}
