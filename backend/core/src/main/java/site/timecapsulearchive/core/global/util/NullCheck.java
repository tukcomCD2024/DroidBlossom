package site.timecapsulearchive.core.global.util;

import java.util.Objects;
import java.util.Optional;
import site.timecapsulearchive.core.global.error.exception.NullCheckValidateException;

public final class NullCheck<T> {

    private NullCheck() {
    }

    public static <T> T validate(T value, String message) {
        try {
            return Optional.of(Objects.requireNonNull(value, message)).get();
        } catch (NullPointerException e) {
            throw new NullCheckValidateException(e.getMessage());
        }
    }
}
