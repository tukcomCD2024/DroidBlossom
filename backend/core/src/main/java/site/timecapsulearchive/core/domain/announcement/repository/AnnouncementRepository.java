package site.timecapsulearchive.core.domain.announcement.repository;

import static site.timecapsulearchive.core.domain.announcement.entity.QAnnouncement.announcement;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.announcement.data.dto.AnnouncementDto;

@Repository
@RequiredArgsConstructor
public class AnnouncementRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<AnnouncementDto> findAll() {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    AnnouncementDto.class,
                    announcement.title,
                    announcement.content,
                    announcement.version,
                    announcement.createdAt
                )
            )
            .from(announcement)
            .fetch();
    }
}
