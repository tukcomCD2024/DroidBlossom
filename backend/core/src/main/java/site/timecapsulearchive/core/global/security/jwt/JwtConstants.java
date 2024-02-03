package site.timecapsulearchive.core.global.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtConstants {
    MEMBER_ID("memberId"),
    TOKEN_TYPE("Bearer "),
    MEMBER_INFO_KEY("memberInfoKey");

    private final String value;
}