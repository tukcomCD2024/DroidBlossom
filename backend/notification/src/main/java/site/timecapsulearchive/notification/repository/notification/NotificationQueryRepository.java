package site.timecapsulearchive.notification.repository.notification;

import java.util.List;
import site.timecapsulearchive.notification.entity.Notification;

public interface NotificationQueryRepository {

    void bulkSave(List<Notification> notifications);

}
