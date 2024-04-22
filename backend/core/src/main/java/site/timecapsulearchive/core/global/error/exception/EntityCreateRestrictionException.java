package site.timecapsulearchive.core.global.error.exception;

import lombok.Getter;
import site.timecapsulearchive.core.global.error.ErrorCode;

@Getter
public class EntityCreateRestrictionException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String className;
    private final Object[] inputs;

    public EntityCreateRestrictionException(String className, Object... inputs) {
        super("엔티티 생성 제약을 위반했습니다. 입력값 확인이 필요합니다.");
        this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        this.className = className;
        this.inputs = inputs;
    }
}
