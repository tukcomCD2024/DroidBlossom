package site.timecapsulearchive.core.domain.capsule.group_capsule.data.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import org.hibernate.validator.constraints.Range;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleCreateRequestDto;
import site.timecapsulearchive.core.global.common.valid.annotation.Image;
import site.timecapsulearchive.core.global.common.valid.annotation.Video;
import site.timecapsulearchive.core.infra.map.data.dto.AddressData;

@Schema(description = "그룹 캡슐 생성 포맷")
public record GroupCapsuleCreateRequest(

    @Schema(description = "업로드한 이미지 경로 ex) capsule/1/xxxx.jpg")
    List<@Image String> imageNames,

    @Schema(description = "업로드한 비디오 경로 ex) capsule/1/xxxx.mp4")
    List<@Video String> videoNames,

    @Schema(description = "캡슐 스킨 아이디")
    @NotNull(message = "캡슐 스킨 아이디는 필수입니다.")
    Long capsuleSkinId,

    @Schema(description = "제목")
    @NotBlank(message = "캡슐 제목은 필수 입니다.")
    String title,

    @Schema(description = "내용")
    @NotBlank(message = "캡슐 내용은 필수 입니다.")
    String content,

    @Range(min = -180, max = 180)
    @NotNull(message = "현재 경도는 필수 입니다.")
    @Schema(description = "현재 경도(wsg84)")
    Double longitude,

    @Range(min = -90, max = 90)
    @NotNull(message = "현재 경도는 필수 입니다.")
    @Schema(description = "현재 위도(wsg84)")
    Double latitude,

    @Schema(description = "캡슐 생성 주소")
    @NotNull(message = "캡슐 생성 주소는 필수 입니다.")
    AddressData addressData,

    @Schema(description = "개봉일")
    ZonedDateTime dueDate
) {

    public GroupCapsuleCreateRequestDto toGroupCapsuleCreateRequestDto() {
        return GroupCapsuleCreateRequestDto.builder()
            .videoNames(videoNames)
            .imageNames(imageNames)
            .capsuleSkinId(capsuleSkinId)
            .title(title)
            .content(content)
            .longitude(longitude)
            .latitude(latitude)
            .addressData(addressData)
            .dueDate(dueDate)
            .build();
    }
}