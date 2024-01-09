package site.timecapsulearchive.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.notification.entity.NotificationCategory;

public interface NotificationCategoryRepository extends JpaRepository<NotificationCategory, Long> {

}
