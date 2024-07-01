package site.timecapsulearchive.core.domain.notification.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.UnaryOperator;
import site.timecapsulearchive.core.domain.notification.data.dto.MemberNotificationDto;
import site.timecapsulearchive.core.global.common.response.ResponseMappingConstant;

@Schema(description = "나의 알림 목록 응답")
public record MemberNotificationSliceResponse(

    @Schema(description = "나의 알림 목록")
    List<MemberNotificationResponse> responseList,

    @Schema(description = "다음 알림 목록 여부")
    boolean hasNext
) {

    public static MemberNotificationSliceResponse createOf(
        final List<MemberNotificationDto> content,
        final boolean hasNext,
        final UnaryOperator<String> singlePreSignUrlFunction
    ) {
        List<MemberNotificationResponse> responses = content.stream()
            .map(dto -> {
                    String imageUrl = "";
                    if (dto.imageUrl() != null) {
                        imageUrl = singlePreSignUrlFunction.apply(dto.imageUrl());
                    }

                    return MemberNotificationResponse.builder()
                        .title(dto.title())
                        .text(dto.text())
                        .createdAt(dto.createdAt().withZoneSameInstant(ResponseMappingConstant.ZONE_ID))
                        .imageUrl(imageUrl)
                        .categoryName(dto.categoryName())
                        .status(dto.status())
                        .build();
                }
            )
            .toList();

        return new MemberNotificationSliceResponse(responses, hasNext);
    }
}
