package site.timecapsulearchive.core.domain.friend.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class FriendDuplicateIdException extends BusinessException {

    public FriendDuplicateIdException() {
        super(ErrorCode.FRIEND_DUPLICATE_ID_ERROR);
    }
}
