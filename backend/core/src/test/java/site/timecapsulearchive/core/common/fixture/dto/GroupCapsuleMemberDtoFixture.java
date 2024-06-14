package site.timecapsulearchive.core.common.fixture.dto;

import java.util.ArrayList;
import java.util.List;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleMemberDto;

public class GroupCapsuleMemberDtoFixture {

    /**
     * 그룹 캡슐에 대한 그룹원 DTO Fixture를 만든다.
     * <br>
     * 맨 처음 사용자(dataPrefix)가 그룹장으로 설정된다.
     * <br>
     * 그룹 캡슐 개봉 상태는 전부 닫힌 상태이다.
     * @param dataPrefix 데이터에 붙여지는 prefix
     * @param size dto 크기
     * @return 그룹 캡슐에 대한 그룹원 목록
     */
    public static List<GroupCapsuleMemberDto> members(int dataPrefix, int size) {
        List<GroupCapsuleMemberDto> result = new ArrayList<>();
        for (int count = dataPrefix; count < dataPrefix + size; count++) {
            boolean isOwner = count == dataPrefix;

            result.add(new GroupCapsuleMemberDto((long) count, count + "nickname",
                count + "profileUrl", isOwner, false));
        }

        return result;
    }
}
