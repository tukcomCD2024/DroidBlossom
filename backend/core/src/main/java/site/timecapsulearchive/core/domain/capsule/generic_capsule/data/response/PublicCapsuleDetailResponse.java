package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;

@Schema(description = "공개 캡슐 상세 정보")
@Builder
public record PublicCapsuleDetailResponse(

    @Schema(description = "공개 캡슐 id")
    Long capsuleId,

    @Schema(description = "캡슐 스킨 url")
    String capsuleSkinUrl,

    @Schema(description = "개봉일")
    ZonedDateTime dueDate,

    @Schema(description = "생성자 닉네임")
    String nickname,

    @Schema(description = "생성자 프로필 url")
    String profileUrl,

    @Schema(description = "생성일")
    ZonedDateTime createdDate,

    @Schema(description = "캡슐 생성 주소")
    String address,

    @Schema(description = "캡슐 생성 도로 이름")
    String roadName,

    @Schema(description = "제목")
    String title,

    @Schema(description = "내용")
    String content,

    @Schema(description = "이미지 url들")
    List<String> imageUrls,

    @Schema(description = "비디오 url들")
    List<String> videoUrls,

    @Schema(description = "개봉 여부")
    Boolean isOpened,

    @Schema(description = "캡슐 타입")
    CapsuleType capsuleType
) {

}