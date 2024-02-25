package site.timecapsulearchive.notification.repository;

import org.springframework.data.repository.Repository;
import site.timecapsulearchive.notification.entity.CategoryName;
import site.timecapsulearchive.notification.entity.NotificationCategory;

public interface NotificationCategoryRepository extends Repository<NotificationCategory, Long> {

    NotificationCategory findByCategoryName(CategoryName name);
}
