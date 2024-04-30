package site.timecapsulearchive.core.common.fixture.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import site.timecapsulearchive.core.domain.member.entity.CategoryName;
import site.timecapsulearchive.core.domain.member.entity.NotificationCategory;

public class NotificationCategoryFixture {

    public static NotificationCategory notificationCategory(CategoryName categoryName) {
        try {
            Constructor<NotificationCategory> declaredConstructor = NotificationCategory.class.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            NotificationCategory notificationCategory = declaredConstructor.newInstance();

            setFieldValue(notificationCategory, "categoryName", categoryName);
            setFieldValue(notificationCategory, "categoryDescription", categoryName.name());

            return notificationCategory;
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
