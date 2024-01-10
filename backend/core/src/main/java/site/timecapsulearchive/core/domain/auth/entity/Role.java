package site.timecapsulearchive.core.domain.auth.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST("ROLE_GUEST", "비사용자"),
    USER("ROLE_USER", "사용자");

    private final String key;
    private final String title;
}
