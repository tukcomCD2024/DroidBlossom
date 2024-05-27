package site.timecapsulearchive.core.common.fixture.dto;

import java.util.List;
import site.timecapsulearchive.core.domain.member_group.data.request.SendGroupRequest;

public class MemberGroupDtoFixture {

    public static SendGroupRequest sendGroupRequest(Long groupId, List<Long> targetIds) {
        return new SendGroupRequest(groupId, targetIds);
    }
}
