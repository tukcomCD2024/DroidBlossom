package site.timecapsulearchive.core.domain.member.data.dto;

import lombok.Builder;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.global.common.wrapper.ByteArrayWrapper;

@Builder
public record MemberDetailDto(
    String nickname,
    String profileUrl,
    String tag,
    SocialType socialType,
    ByteArrayWrapper email,
    ByteArrayWrapper phone,
    Long friendCount,
    Long groupCount,
    Boolean tagSearchAvailable,
    Boolean phoneSearchAvailable
) {

}
