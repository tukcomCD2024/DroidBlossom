package site.timecapsulearchive.core.global.common.converter;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class ZonedDateTimeConverter implements Converter<String, ZonedDateTime> {

    @Override
    public ZonedDateTime convert(String source) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(source, DateTimeFormatter.ISO_DATE_TIME);

        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
    }
}
