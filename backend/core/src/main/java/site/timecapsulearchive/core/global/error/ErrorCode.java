package site.timecapsulearchive.core.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //global
    INTERNAL_SERVER_ERROR(500, "G001", "서버에 오류가 발생하였습니다."),
    INPUT_INVALID_VALUE_ERROR(400, "G002", "잘못된 입력 값입니다."),
    INPUT_INVALID_TYPE_ERROR(400, "G003", "잘못된 입력 타입입니다."),

    //jwt
    INVALID_TOKEN_ERROR(400, "J001", "jwt 토큰이 유효하지 않습니다."),
    ALREADY_RE_ISSUED_TOKEN_ERROR(400, "J002", "이미 액세스 토큰 재발급에 사용된 리프레시 토큰입니다."),

    //auth
    AUTHENTICATION_ERROR(401, "A001", "인증에 실패했습니다. 인증 수단이 유효한지 확인하세요."),

    //message
    TOO_MANY_REQUEST_ERROR(429, "M001", "너무 많은 인증 메시지를 요청했습니다. 24시간 후 요청해주세요."),
    CERTIFICATION_NUMBER_NOT_FOUND_ERROR(404, "M002", "인증 번호를 찾지 못하였습니다."),
    CERTIFICATION_NUMBER_NOT_MATCH_ERROR(400, "M003", "인증 번호가 일치하지 않습니다."),

    //ouath
    OAUTH2_NOT_AUTHENTICATED_ERROR(401, "O001", "OAuth2 인증에 실패하였습니다."),

    //외부 API
    EXTERNAL_API_ERROR(500, "A001", "외부 api 호출에 실패했습니다. 잠시 후 요청해주세요."),

    //member
    NOT_FOUND_MEMBER_ERROR(404, "U002", "사용자 데이터를 찾지 못하였습니다."),
    LOGIN_ON_NOT_VERIFIED_ERROR(400, "U003", "인증되지 않은 사용자로 로그인을 시도했습니다."),
    ALREADY_VERIFIED_ERROR(400, "U004", "이미 인증된 사용자입니다."),
    MEMBER_NOT_FOUND_ERROR(404, "U002", "사용자 데이터를 찾지 못하였습니다."),

    //geo
    GEO_TRANSFORMED_ERROR(400, "GEO-001", "좌표 변환에 실패했습니다. 입력이 유효한지 확인해주세요"),

    //capsule skin
    CAPSULE_SKIN_NOT_FOUND_ERROR(404, "C001", "캡슐 스킨을 찾지 못하였습니다.");

    private final int status;
    private final String code;
    private final String message;
}
