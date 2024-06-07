package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import lombok.Getter;

@Getter
public enum CapsuleOpenStatus {
    OPEN("캡슐이 개봉되었습니다."), NOT_OPEN("캡슐이 개봉되지 않았습니다.");

    private final String statusMessage;

    CapsuleOpenStatus(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
