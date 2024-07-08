package site.timecapsulearchive.core.domain.member.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.function.Function;
import site.timecapsulearchive.core.domain.member.data.dto.MemberDetailDto;
import site.timecapsulearchive.core.domain.member.entity.SocialType;

@Schema(description = "회원 상세 정보")
public record MemberDetailResponse(

    @Schema(description = "닉네임")
    String nickname,

    @Schema(description = "프로필 url")
    String profileUrl,

    @Schema(description = "검색 태그")
    String tag,

    @Schema(description = "소셜 타입")
    SocialType socialType,

    @Schema(description = "이메일")
    String email,

    @Schema(description = "전화번호")
    String phone,

    @Schema(description = "친구수")
    Long friendCount,

    @Schema(description = "그룹수")
    Long groupCount
) {

    public static MemberDetailResponse createOf(
        final MemberDetailDto detailDto,
        final Function<byte[], String> phoneDecryption
    ) {
        return new MemberDetailResponse(
            detailDto.nickname(),
            detailDto.profileUrl(),
            detailDto.tag(),
            detailDto.socialType(),
            detailDto.email(),
            phoneDecryption.apply(detailDto.phone().data()),
            detailDto.friendCount(),
            detailDto.groupCount()
        );
    }
}