package site.timecapsulearchive.core.global.common.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import site.timecapsulearchive.core.global.common.valid.annotation.Image;

public class ImageExtensionValidator implements ConstraintValidator<Image, String> {

    private static final String AVAILABLE_IMAGE_EXTENSION_REGEX = "^\\S+\\.png$";

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        return Pattern.matches(AVAILABLE_IMAGE_EXTENSION_REGEX, value);
    }
}
