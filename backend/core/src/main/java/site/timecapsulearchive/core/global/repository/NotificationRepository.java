package site.timecapsulearchive.core.global.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.core.global.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
