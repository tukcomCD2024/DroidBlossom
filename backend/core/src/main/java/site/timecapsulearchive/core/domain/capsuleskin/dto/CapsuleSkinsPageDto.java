package site.timecapsulearchive.core.domain.capsuleskin.dto;

import java.util.List;

public record CapsuleSkinsPageDto(
    List<CapsuleSkinSummaryDto> capsuleSkins,
    boolean hasNext
) {

    public static CapsuleSkinsPageDto from(
        List<CapsuleSkinSummaryDto> capsuleSkins,
        boolean hasNext
    ) {
        return new CapsuleSkinsPageDto(capsuleSkins, hasNext);
    }
}
