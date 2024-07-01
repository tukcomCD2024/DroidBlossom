package site.timecapsulearchive.core.domain.notification.repository;

import java.time.ZonedDateTime;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import site.timecapsulearchive.core.domain.notification.entity.Notification;

public interface NotificationRepository extends Repository<Notification, Long>,
    NotificationQueryRepository {

    @Query("UPDATE Notification n SET n.deletedAt = :deletedAt WHERE n.member.id = :memberId")
    @Modifying
    void deleteByMemberId(
        @Param("memberId") Long memberId,
        @Param("deletedAt") ZonedDateTime deletedAt
    );
}
