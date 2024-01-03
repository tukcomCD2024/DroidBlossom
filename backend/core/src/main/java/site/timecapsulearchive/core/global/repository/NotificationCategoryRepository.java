package site.timecapsulearchive.core.global.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.core.global.entity.NotificationCategory;

public interface NotificationCategoryRepository extends JpaRepository<NotificationCategory, Long> {

}
