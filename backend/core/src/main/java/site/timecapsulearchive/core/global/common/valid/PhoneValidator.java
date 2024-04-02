package site.timecapsulearchive.core.global.common.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Pattern;
import site.timecapsulearchive.core.global.common.valid.annotation.Phone;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private static final String PHONE_REGEX = "^\\d{11}$";
    private static final Pattern pattern = Pattern.compile(PHONE_REGEX);

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (Objects.isNull(value) || value.isBlank()) {
            return false;
        }

        return pattern.matcher(value).matches();
    }
}
