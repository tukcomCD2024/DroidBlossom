package site.timecapsulearchive.core.domain.notification.repository;

import static site.timecapsulearchive.core.domain.notification.entity.QNotification.notification;
import static site.timecapsulearchive.core.domain.notification.entity.QNotificationCategory.notificationCategory;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.notification.data.dto.MemberNotificationDto;
import site.timecapsulearchive.core.global.util.SliceUtil;

@Repository
@RequiredArgsConstructor
public class NotificationQueryRepositoryImpl implements NotificationQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<MemberNotificationDto> findNotificationSliceByMemberId(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        final List<MemberNotificationDto> notifications = jpaQueryFactory
            .select(
                Projections.constructor(
                    MemberNotificationDto.class,
                    notification.title,
                    notification.text,
                    notification.createdAt,
                    notification.imageUrl,
                    notificationCategory.categoryName,
                    notification.status
                )
            )
            .from(notification)
            .join(notificationCategory)
            .on(notification.notificationCategory.eq(notificationCategory),
                notificationCategory.deletedAt.isNull())
            .where(notification.createdAt.lt(createdAt).and(notification.member.id.eq(memberId)))
            .orderBy(notification.id.desc())
            .limit(size + 1)
            .fetch();

        return SliceUtil.makeSlice(size, notifications);
    }
}
