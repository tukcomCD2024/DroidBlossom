package site.timecapsulearchive.core.domain.friend.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class FriendTwoWayInviteException extends BusinessException {

    public FriendTwoWayInviteException() {
        super(ErrorCode.FRIEND_TWO_WAY_INVITE_ERROR);
    }
}
