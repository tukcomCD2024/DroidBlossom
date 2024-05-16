package site.timecapsulearchive.core.common.fixture.dto;

import java.util.List;
import site.timecapsulearchive.core.domain.group.data.dto.GroupCreateDto;
import site.timecapsulearchive.core.domain.member_group.data.GroupOwnerSummaryDto;

public class GroupDtoFixture {

    public static GroupCreateDto groupCreateDto(List<Long> targetIds) {
        return GroupCreateDto.builder()
            .groupName("testGroupName")
            .groupProfileUrl("testGroupProfileUrl")
            .groupImage("testGroupImage")
            .description("testDescription")
            .targetIds(targetIds)
            .build();
    }

    public static GroupOwnerSummaryDto groupOwnerSummaryDto(Boolean isOwner) {
        return new GroupOwnerSummaryDto("testNickname", isOwner, "testGroupProfileUrl");
    }

}
