package site.timecapsulearchive.notification.repository.notification;

import org.springframework.data.repository.Repository;
import site.timecapsulearchive.notification.entity.Notification;


public interface NotificationRepository extends Repository<Notification, Long>,
    NotificationQueryRepository {
      
    void save(Notification entity);
}
