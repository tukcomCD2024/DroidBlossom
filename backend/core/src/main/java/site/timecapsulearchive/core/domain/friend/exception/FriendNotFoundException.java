package site.timecapsulearchive.core.domain.friend.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class FriendNotFoundException extends BusinessException {

    public FriendNotFoundException() {
        super(ErrorCode.FRIEND_NOT_FOUND_ERROR);
    }
}
