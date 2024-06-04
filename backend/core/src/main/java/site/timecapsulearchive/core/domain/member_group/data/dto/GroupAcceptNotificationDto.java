package site.timecapsulearchive.core.domain.member_group.data.dto;

public record GroupAcceptNotificationDto(
    String groupMemberNickname,
    Long groupOwnerId
) {

}
