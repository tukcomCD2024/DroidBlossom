package site.timecapsulearchive.core.domain.announcement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Test;
import site.timecapsulearchive.core.common.fixture.domain.AnnouncementTestFixture;
import site.timecapsulearchive.core.domain.announcement.data.dto.AnnouncementDto;
import site.timecapsulearchive.core.domain.announcement.repository.AnnouncementRepository;

class AnnouncementServiceTest {

    private final AnnouncementRepository announcementRepository = mock(
        AnnouncementRepository.class);
    private final AnnouncementService announcementService = new AnnouncementService(
        announcementRepository);

    @Test
    void 공지사항을_조회하면_가장_최근에_생성된_공지사항이_최상단에_위치한다() {
        //given
        given(announcementRepository.findAll()).willReturn(AnnouncementTestFixture.announcementDtos(10));

        //when
        List<AnnouncementDto> announcements = announcementService.findAll();

        //then
        assertThat(announcements)
            .isSortedAccordingTo(Comparator.comparing(AnnouncementDto::createdAt).reversed());
    }
}
