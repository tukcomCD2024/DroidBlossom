package site.timecapsulearchive.core.infra.notification.data.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinCreateDto;
import site.timecapsulearchive.core.infra.notification.data.dto.request.CreatedCapsuleSkinNotificationRequest;
import site.timecapsulearchive.core.infra.notification.data.dto.request.FriendReqNotificationRequest;
import site.timecapsulearchive.core.infra.s3.manager.S3UrlGenerator;

@Component
@RequiredArgsConstructor
public class NotificationMapper {

    private final S3UrlGenerator s3UrlGenerator;

    public CreatedCapsuleSkinNotificationRequest capsuleSkinDtoToMessage(Long memberId,
        CapsuleSkinCreateDto dto) {
        return CreatedCapsuleSkinNotificationRequest.builder()
            .memberId(memberId)
            .status("SUCCESS_MAKE_CAPSULE_SKIN")
            .skinName(dto.skinName())
            .title("캡슐 스킨 생성 알림")
            .text(dto.skinName() + "이 생성되었습니다. ARchive에서 확인해보세요!")
            .skinUrl(s3UrlGenerator.generateFileName(memberId, dto.directory(), dto.skinName()))
            .build();
    }

    public FriendReqNotificationRequest friendReqToMessage(Long friendId, String ownerNickname) {
        return FriendReqNotificationRequest.builder()
            .friendId(friendId)
            .status("SEND_FRIEND_REQ_MESSAGE")
            .title("친구 요청 알림")
            .text(ownerNickname + "로부터 친구 요청이 왔습니다. ARchive에서 확인해보세요!")
            .build();
    }
}
