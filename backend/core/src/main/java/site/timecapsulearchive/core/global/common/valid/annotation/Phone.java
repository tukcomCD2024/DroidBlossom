package site.timecapsulearchive.core.global.common.valid.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import site.timecapsulearchive.core.global.common.valid.PhoneValidator;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
@Documented
public @interface Phone {

    String message() default "Invalid Phone";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
