package site.timecapsulearchive.core.domain.member.data.dto;

import java.util.Objects;
import lombok.Builder;

@Builder
public record MemberDetailDto(
    String nickname,
    String profileUrl,
    String tag,
    Long friendCount,
    Long groupCount
) {
    public MemberDetailDto {
        Objects.requireNonNull(nickname);
        Objects.requireNonNull(profileUrl);
        Objects.requireNonNull(tag);
        Objects.requireNonNull(friendCount);
        Objects.requireNonNull(groupCount);
    }
}
