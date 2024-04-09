package site.timecapsulearchive.core.domain.member.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import site.timecapsulearchive.core.domain.member.data.dto.MemberDetailDto;

@Schema(description = "회원 상세 정보")
public record MemberDetailResponse(

    @Schema(description = "닉네임")
    String nickname,

    @Schema(description = "프로필 url")
    String profileUrl,

    @Schema(description = "검색 태그")
    String tag,

    @Schema(description = "친구수")
    Long friendCount,

    @Schema(description = "그룹수")
    Long groupCount
) {

    public static MemberDetailResponse createOf(MemberDetailDto detailDto) {
        return new MemberDetailResponse(
            detailDto.nickname(),
            detailDto.profileUrl(),
            detailDto.tag(),
            detailDto.friendCount(),
            detailDto.groupCount()
        );
    }
}