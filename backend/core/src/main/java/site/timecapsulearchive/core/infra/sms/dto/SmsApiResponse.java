package site.timecapsulearchive.core.infra.sms.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record SmsApiResponse(
    Integer resultCode,
    String message
) {

}
