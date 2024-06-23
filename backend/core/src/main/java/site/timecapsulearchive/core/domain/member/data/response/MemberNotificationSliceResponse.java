package site.timecapsulearchive.core.domain.member.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.UnaryOperator;
import org.springframework.data.domain.Slice;
import site.timecapsulearchive.core.domain.member.data.dto.MemberNotificationDto;

@Schema(description = "나의 알림 목록 응답")
public record MemberNotificationSliceResponse(

    @Schema(description = "나의 알림 목록")
    List<MemberNotificationResponse> responseList,

    @Schema(description = "다음 알림 목록 여부")
    boolean hasNext
) {

    public static MemberNotificationSliceResponse createOf(
        final Slice<MemberNotificationDto> memberNotificationDtos,
        final UnaryOperator<String> getS3PreSignedUrlForGet
    ) {
        final List<MemberNotificationResponse> responses = memberNotificationDtos.getContent()
            .stream()
            .map(dto -> dto.toResponse(getS3PreSignedUrlForGet))
            .toList();

        return new MemberNotificationSliceResponse(responses, memberNotificationDtos.hasNext());
    }
}
