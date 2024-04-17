package site.timecapsulearchive.core.domain.friend.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "연락처")
public record PhoneBook(

    @Schema(description = "전화번호")
    String originPhone,
    @Schema(description = "이름")
    String originName
) {

}
