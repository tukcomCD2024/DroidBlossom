package site.timecapsulearchive.core.global.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //global
    INTERNAL_SERVER_ERROR("G001", "서버에 오류가 발생하였습니다."),

    //jwt
    INVALID_TOKEN_EXCEPTION("J001", "jwt 토큰이 유효하지 않습니다."),
    ALREADY_RE_ISSUED_TOKEN_EXCEPTION("J002", "이미 액세스 토큰 재발급에 사용된 리프레시 토큰입니다."),

    //ouath
    OAUTH2_NOT_AUTHENTICATED_EXCEPTION("O001", "OAuth2 인증에 실패하였습니다.");

    private final String code;
    private final String message;
}
