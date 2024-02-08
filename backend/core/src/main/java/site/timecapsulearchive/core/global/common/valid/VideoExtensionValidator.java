package site.timecapsulearchive.core.global.common.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import site.timecapsulearchive.core.global.common.valid.annotation.Video;

public class VideoExtensionValidator implements ConstraintValidator<Video, String> {

    private static final String AVAILABLE_VIDEO_EXTENSION_REGEX = "^\\S+\\.mp4$";

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        return Pattern.matches(AVAILABLE_VIDEO_EXTENSION_REGEX, value);
    }
}
