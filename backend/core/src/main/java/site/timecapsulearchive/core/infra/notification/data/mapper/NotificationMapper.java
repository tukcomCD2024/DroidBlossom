package site.timecapsulearchive.core.infra.notification.data.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinCreateDto;
import site.timecapsulearchive.core.infra.notification.data.dto.request.CreatedCapsuleSkinNotificationRequest;
import site.timecapsulearchive.core.infra.s3.manager.S3UrlGenerator;

@Component
@RequiredArgsConstructor
public class NotificationMapper {

    private final S3UrlGenerator s3UrlGenerator;

    public CreatedCapsuleSkinNotificationRequest toRequest(Long memberId,
        CapsuleSkinCreateDto dto) {
        return new CreatedCapsuleSkinNotificationRequest(
            memberId,
            "SUCCESS_MAKE_CAPSULE_SKIN",
            dto.skinName(),
            "캡슐 스킨 생성이 완료되었습니다",
            dto.skinName() + "이 생성되었습니다. ARchive에서 확인해보세요!",
            s3UrlGenerator.generateFileName(memberId, dto.directory(), dto.skinName())
        );
    }
}
