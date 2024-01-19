package site.timecapsulearchive.core.global.common.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import site.timecapsulearchive.core.global.common.valid.annotation.Phone;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private static final String PHONE_REGEX = "^01(?:0|1|[6-9])?(\\d{3}|\\d{4})?(\\d{4})$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Pattern.matches(value, PHONE_REGEX);
    }
}
