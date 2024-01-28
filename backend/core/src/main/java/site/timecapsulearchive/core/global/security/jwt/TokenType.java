package site.timecapsulearchive.core.global.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenType {
    ACCESS("access"), REFRESH("refresh"), TEMPORARY("temporary");

    private final String value;
}
