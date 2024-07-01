package site.timecapsulearchive.core.common.fixture.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.notification.entity.Notification;
import site.timecapsulearchive.core.domain.notification.entity.NotificationCategory;
import site.timecapsulearchive.core.domain.notification.entity.NotificationStatus;

public class NotificationFixture {

    public static Notification notification(Member member,
        NotificationCategory notificationCategory) {
        try {
            Constructor<Notification> declaredConstructor = Notification.class.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            Notification notification = declaredConstructor.newInstance();

            setFieldValue(notification, "title", "title");
            setFieldValue(notification, "text", "text");
            setFieldValue(notification, "imageUrl", "imageUrl");
            setFieldValue(notification, "status", NotificationStatus.SUCCESS);
            setFieldValue(notification, "member", member);
            setFieldValue(notification, "notificationCategory", notificationCategory);

            return notification;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setFieldValue(Object instance, String fieldName, Object value)
        throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }
}
