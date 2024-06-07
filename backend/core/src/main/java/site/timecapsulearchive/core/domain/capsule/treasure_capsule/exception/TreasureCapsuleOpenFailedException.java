package site.timecapsulearchive.core.domain.capsule.treasure_capsule.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class TreasureCapsuleOpenFailedException extends BusinessException {

    public TreasureCapsuleOpenFailedException() {
        super(ErrorCode.TREASURE_CAPSULE_OPEN_FAILED_ERROR);
    }
}
