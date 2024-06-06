package site.timecapsulearchive.core.domain.friend.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class SelfFriendOperationException extends BusinessException {

    public SelfFriendOperationException() {
        super(ErrorCode.SELF_FRIEND_OPERATION_ERROR);
    }
}
