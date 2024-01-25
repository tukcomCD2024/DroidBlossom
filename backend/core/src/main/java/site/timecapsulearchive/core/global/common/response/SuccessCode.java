package site.timecapsulearchive.core.global.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    //success handle
    SUCCESS("00", "요청 처리에 성공했습니다."),
    ACCEPTED("01", "요청이 수락되었습니다.");

    private final String code;
    private final String message;
}
