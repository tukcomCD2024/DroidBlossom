package site.timecapsulearchive.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.notification.entity.Notification;

public interface NotificationRepository extends Repository<Notification, Long> {

    void save(Notification entity);
}
