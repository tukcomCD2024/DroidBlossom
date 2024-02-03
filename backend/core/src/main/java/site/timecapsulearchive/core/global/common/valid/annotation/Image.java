package site.timecapsulearchive.core.global.common.valid.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import site.timecapsulearchive.core.global.common.valid.ImageExtensionValidator;

@Target({ElementType.TYPE_USE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageExtensionValidator.class)
@Documented
public @interface Image {

    String message() default "이미지는 jpeg만 지원합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
