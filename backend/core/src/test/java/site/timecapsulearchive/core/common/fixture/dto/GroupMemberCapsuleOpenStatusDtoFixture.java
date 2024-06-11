package site.timecapsulearchive.core.common.fixture.dto;

import java.util.ArrayList;
import java.util.List;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupMemberCapsuleOpenStatusDto;

public class GroupMemberCapsuleOpenStatusDtoFixture {

    public static List<GroupMemberCapsuleOpenStatusDto> groupMemberCapsuleOpenStatusDto(
        Long memberId,
        int size
    ) {
        List<GroupMemberCapsuleOpenStatusDto> result = new ArrayList<>();
        for (long count = memberId; count < size; count++) {
            result.add(new GroupMemberCapsuleOpenStatusDto(count, count + "test-nickname",
                count + "test-profile", true));
        }

        return result;
    }
}
