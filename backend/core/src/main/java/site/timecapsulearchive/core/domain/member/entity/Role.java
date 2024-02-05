package site.timecapsulearchive.core.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER"), TEMPORARY("ROLE_TEMPORARY");

    private final String value;
}
