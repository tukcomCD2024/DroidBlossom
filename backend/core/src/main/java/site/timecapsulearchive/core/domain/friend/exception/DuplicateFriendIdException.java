package site.timecapsulearchive.core.domain.friend.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class DuplicateFriendIdException extends BusinessException {

    public DuplicateFriendIdException() {
        super(ErrorCode.FRIEND_DUPLICATE_ID_ERROR);
    }
}
