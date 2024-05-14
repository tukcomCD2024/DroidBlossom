package site.timecapsulearchive.core.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //global
    INTERNAL_SERVER_ERROR(500, "GLOBAL-001", "서버에 오류가 발생하였습니다."),
    INPUT_INVALID_VALUE_ERROR(400, "GLOBAL-002", "잘못된 입력 값입니다."),
    INPUT_INVALID_TYPE_ERROR(400, "GLOBAL-003", "잘못된 입력 타입입니다."),
    REQUEST_PARAMETER_NOT_FOUND_ERROR(400, "GLOBAL-004", "입력 파라미터가 존재하지 않습니다."),
    REQUEST_PARAMETER_TYPE_NOT_MATCH_ERROR(400, "GLOBAL-005", "입력 파라미터의 타입이 올바르지 않습니다."),

    //jwt
    INVALID_TOKEN_ERROR(400, "AUTH-001", "jwt 토큰이 유효하지 않습니다."),
    ALREADY_RE_ISSUED_TOKEN_ERROR(400, "AUTH-002", "이미 액세스 토큰 재발급에 사용된 리프레시 토큰입니다."),

    //auth
    AUTHENTICATION_ERROR(401, "AUTH-003", "인증에 실패했습니다. 인증 수단이 유효한지 확인하세요."),
    AUTHORIZATION_ERROR(403, "AUTH-004", "권한이 존재하지 않습니다."),
    CREDENTIALS_NOT_MATCHED_ERROR(401, "AUTH-005", "이메일과 비밀번호 인증에 실패했습니다."),

    //message
    TOO_MANY_REQUEST_ERROR(429, "MESSAGE-001", "너무 많은 인증 메시지를 요청했습니다. 24시간 후 요청해주세요."),

    CERTIFICATION_NUMBER_NOT_FOUND_ERROR(404, "MESSAGE-002", "인증 번호를 찾지 못하였습니다."),

    CERTIFICATION_NUMBER_NOT_MATCH_ERROR(400, "MESSAGE-003", "인증 번호가 일치하지 않습니다."),

    //ouath
    OAUTH2_NOT_AUTHENTICATED_ERROR(401, "AUTH-004", "OAuth2 인증에 실패하였습니다."),

    //외부 API
    EXTERNAL_API_ERROR(500, "EXTERNAL-001", "외부 api 호출에 실패했습니다. 잠시 후 요청해주세요."),

    //member
    LOGIN_ON_NOT_VERIFIED_ERROR(400, "MEMBER-001", "인증되지 않은 사용자로 로그인을 시도했습니다."),

    ALREADY_VERIFIED_ERROR(400, "MEMBER-002", "이미 인증된 사용자입니다."),

    MEMBER_NOT_FOUND_ERROR(404, "MEMBER-003", "사용자 데이터를 찾지 못하였습니다."),

    //geo
    GEO_TRANSFORMED_ERROR(400, "GEO-001", "좌표 변환에 실패했습니다. 입력이 유효한지 확인해주세요"),

    //capsule skin
    CAPSULE_SKIN_NOT_FOUND_ERROR(404, "SKIN-001", "캡슐 스킨을 찾지 못하였습니다."),

    //capsule
    CAPSULE_NOT_FOUND_ERROR(404, "CAPSULE-001", "캡슐을 찾지 못하였습니다."),
    NO_CAPSULE_AUTHORITY_ERROR(403, "CAPSULE-002", "캡슐에 접근 권한이 없습니다."),

    //friend
    FRIEND_NOT_FOUND_ERROR(404, "FRIEND-001", "친구를 찾지 못하였습니다"),
    FRIEND_DUPLICATE_ID_ERROR(404, "FRIEND-002", "친구 아이디가 중복되었습니다."),

    //group
    GROUP_CREATE_ERROR(400, "GROUP-001", "그룹 생성에 실패하였습니다."),
    GROUP_NOT_FOUND_ERROR(404, "GROUP-002", "그룹을 찾을 수 없습니다"),
    GROUP_INVITATION_NOT_FOUND_ERROR(404, "GROUP-003", "그룹 초대를 찾을 수 없습니다"),
    GROUP_MEMBER_EXIST_ERROR(400, "GROUP-004", "다른 그룹 멤버가 존재해 그룹을 삭제할 수 없습니다."),
    NO_GROUP_AUTHORITY_ERROR(403, "GROUP-005", "그룹에 대한 권한이 존재하지 않습니다."),
    GROUP_CAPSULE_EXIST_ERROR(400, "GROUP-006", "그룹 캡슐이 존재해 그룹을 삭제할 수 없습니다."),
    GROUP_OWNER_QUIT_ERROR(400, "GROUP-007", "그룹장은 그룹을 탈퇴할 수 없습니다."),
    GROUP_MEMBER_NOT_FOUND_ERROR(404, "GROUP-008", "그룹에서 해당 멤버를 찾을 수 없습니다."),
    GROUP_MEMBER_DUPLICATED_ID_ERROR(400, "GROUP-009", "자신을 추방할 수 없습니다."),

    //friend invite
    FRIEND_INVITE_NOT_FOUND_ERROR(404, "FRIEND-INVITE-001", "친구 요청을 찾지 못하였습니다."),
    FRIEND_TWO_WAY_INVITE_ERROR(400, "FRIEND-INVITE-002", "친구 요청을 받은 상태입니다.");

    private final int status;
    private final String code;
    private final String message;
}
