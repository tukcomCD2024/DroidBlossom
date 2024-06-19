package site.timecapsulearchive.core.domain.friend.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class FriendInviteDuplicateException extends BusinessException {

    public FriendInviteDuplicateException() {
        super(ErrorCode.FRIEND_INVITE_DUPLICATE_ERROR);
    }
}
