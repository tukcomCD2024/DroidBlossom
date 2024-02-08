package site.timecapsulearchive.core.global.common.valid.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import site.timecapsulearchive.core.global.common.valid.VideoExtensionValidator;

@Target({ElementType.TYPE_USE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VideoExtensionValidator.class)
@Documented
public @interface Video {

    String message() default "비디오는 공백을 포함하지 않는 mp4만 지원합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
