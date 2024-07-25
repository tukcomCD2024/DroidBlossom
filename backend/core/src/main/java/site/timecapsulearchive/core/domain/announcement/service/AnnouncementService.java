package site.timecapsulearchive.core.domain.announcement.service;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.announcement.data.dto.AnnouncementDto;
import site.timecapsulearchive.core.domain.announcement.repository.AnnouncementRepository;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public List<AnnouncementDto> findAll() {
        List<AnnouncementDto> announcements = announcementRepository.findAll();

        announcements.sort(Comparator.comparing(AnnouncementDto::createdAt).reversed());

        return announcements;
    }
}
