package site.timecapsulearchive.core.infra.notification.data.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinCreateDto;
import site.timecapsulearchive.core.infra.notification.data.dto.request.CreatedCapsuleSkinNotificationRequest;
import site.timecapsulearchive.core.infra.notification.data.dto.request.FriendAcceptNotificationRequest;
import site.timecapsulearchive.core.infra.notification.data.dto.request.FriendReqNotificationRequest;
import site.timecapsulearchive.core.infra.s3.manager.S3UrlGenerator;

@Component
@RequiredArgsConstructor
public class NotificationMapper {

    private static final String NOTIFICATION_SEND_SUCCESS = "SUCCESS";
    private final S3UrlGenerator s3UrlGenerator;

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
            .skinUrl(s3UrlGenerator.generateFileName(memberId, dto.directory(), dto.imageUrl()))
            .build();
    }

    public FriendReqNotificationRequest friendReqToMessage(
        final Long friendId,
        final String ownerNickname
    ) {
        return FriendReqNotificationRequest.builder()
            .friendId(friendId)
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
            .memberId(friendId)
            .status(NOTIFICATION_SEND_SUCCESS)
            .title("친구 수락 알림")
            .text(ownerNickname + "가 친구 요청을 수락하였습니다. ARchive에서 확인해보세요!")
            .build();
    }
}
