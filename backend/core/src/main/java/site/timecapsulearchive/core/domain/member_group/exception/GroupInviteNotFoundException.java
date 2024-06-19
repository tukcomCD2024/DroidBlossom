package site.timecapsulearchive.core.domain.member_group.exception;

import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class GroupInviteNotFoundException extends BusinessException {

    public GroupInviteNotFoundException() {
        super(ErrorCode.GROUP_INVITATION_NOT_FOUND_ERROR);
    }
}
