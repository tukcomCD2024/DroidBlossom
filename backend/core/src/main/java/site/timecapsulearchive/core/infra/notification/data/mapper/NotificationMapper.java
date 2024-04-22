package site.timecapsulearchive.core.infra.notification.data.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinCreateDto;
import site.timecapsulearchive.core.infra.notification.data.dto.request.CreatedCapsuleSkinNotificationRequest;
import site.timecapsulearchive.core.infra.notification.data.dto.request.FriendAcceptNotificationRequest;
import site.timecapsulearchive.core.infra.notification.data.dto.request.FriendReqNotificationRequest;
import site.timecapsulearchive.core.infra.s3.data.dto.S3Directory;

@Component
@RequiredArgsConstructor
public class NotificationMapper {

    private static final String NOTIFICATION_SEND_SUCCESS = "SUCCESS";

    public CreatedCapsuleSkinNotificationRequest capsuleSkinDtoToMessage(
        final Long memberId,
        final CapsuleSkinCreateDto dto
    ) {
        return CreatedCapsuleSkinNotificationRequest.builder()
            .memberId(memberId)
            .status(NOTIFICATION_SEND_SUCCESS)
            .skinName(dto.skinName())
            .title("캡슐 스킨 생성 알림")
            .text(dto.skinName() + "이 생성되었습니다. ARchive에서 확인해보세요!")
            .skinUrl(S3Directory.CAPSULE_SKIN.generateFullPath(memberId, dto.imageFullPath()))
            .build();
    }

    public FriendReqNotificationRequest friendReqToMessage(
        final Long friendId,
        final String ownerNickname
    ) {
        return FriendReqNotificationRequest.builder()
            .targetId(friendId)
            .status(NOTIFICATION_SEND_SUCCESS)
            .title("친구 요청 알림")
            .text(ownerNickname + "로부터 친구 요청이 왔습니다. ARchive에서 확인해보세요!")
            .build();
    }

    public FriendAcceptNotificationRequest friendAcceptToMessage(
        final Long friendId,
        final String ownerNickname
    ) {
        return FriendAcceptNotificationRequest.builder()
            .targetId(friendId)
            .status(NOTIFICATION_SEND_SUCCESS)
            .title("친구 수락 알림")
            .text(ownerNickname + "님이 친구 요청을 수락하였습니다. ARchive에서 확인해보세요!")
            .build();
    }
}
