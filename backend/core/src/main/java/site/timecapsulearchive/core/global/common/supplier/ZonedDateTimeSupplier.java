package site.timecapsulearchive.core.global.common.supplier;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.Supplier;

public class ZonedDateTimeSupplier {

    private static final String TIME_ZONE = "UTC";

    public static Supplier<ZonedDateTime> utc() {
        return () -> ZonedDateTime.now(ZoneId.of(TIME_ZONE));
    }
}
