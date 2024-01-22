package site.timecapsulearchive.core.global.common.valid.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import site.timecapsulearchive.core.global.common.valid.PhoneValidator;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
@Documented
public @interface Phone {

    String message() default "Invalid Phone";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
