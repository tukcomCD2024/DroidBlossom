package site.timecapsulearchive.core.domain.friend.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class FriendInviteNotFoundException extends BusinessException {

    public FriendInviteNotFoundException() {
        super(ErrorCode.FRIEND_INVITE_NOT_FOUND_ERROR);
    }
}
