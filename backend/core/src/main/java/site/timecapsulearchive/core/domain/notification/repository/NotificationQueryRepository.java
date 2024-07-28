package site.timecapsulearchive.core.domain.notification.repository;

import java.time.ZonedDateTime;
import org.springframework.data.domain.Slice;
import site.timecapsulearchive.core.domain.notification.data.dto.MemberNotificationDto;

public interface NotificationQueryRepository {

    Slice<MemberNotificationDto> findNotificationSliceByMemberId(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    );
}
