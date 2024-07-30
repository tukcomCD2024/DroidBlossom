package site.timecapsulearchive.core.domain.notification.service;

import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.notification.data.dto.MemberNotificationDto;
import site.timecapsulearchive.core.domain.notification.repository.NotificationRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void deleteByMemberId(final Long memberId, final ZonedDateTime deletedAt) {
        notificationRepository.deleteByMemberId(memberId, deletedAt);
    }

    public Slice<MemberNotificationDto> findNotificationSliceByMemberId(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return notificationRepository.findNotificationSliceByMemberId(
            memberId, size, createdAt);
    }
}
