package site.timecapsulearchive.core.domain.group.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.Function;
import lombok.Builder;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberDto;

@Builder
@Schema(description = "그룹원들 정보 리스트")
public record GroupMemberInfosResponse(

    @Schema(description = "그룹원들 정보")
    List<GroupMemberInfoResponse> groupMemberResponses
) {

    public static GroupMemberInfosResponse createOf(
        final List<GroupMemberDto> groupMemberDtos,
        final Function<String, String> singlePreSignUrlFunction
    ) {
        List<GroupMemberInfoResponse> groupMemberResponses = groupMemberDtos.stream()
            .map(dto -> dto.toInfoResponse(singlePreSignUrlFunction))
            .toList();

        return new GroupMemberInfosResponse(groupMemberResponses);
    }
}