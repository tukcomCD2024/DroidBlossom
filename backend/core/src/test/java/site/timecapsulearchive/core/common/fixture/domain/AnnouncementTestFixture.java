package site.timecapsulearchive.core.common.fixture.domain;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import site.timecapsulearchive.core.domain.announcement.data.dto.AnnouncementDto;

public class AnnouncementTestFixture {

    public static List<AnnouncementDto> announcementDtos(int size) {
        List<AnnouncementDto> result = new ArrayList<>();
        ZonedDateTime now = ZonedDateTime.now();
        for (int index = 0; index < size; index++) {
            result.add(
                new AnnouncementDto("title" + index, "content" + index, String.valueOf(index),
                    now.plusSeconds(index))
            );
        }

        return result;
    }
}
