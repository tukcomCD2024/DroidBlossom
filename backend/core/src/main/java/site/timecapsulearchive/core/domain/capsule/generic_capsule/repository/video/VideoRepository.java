package site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.video;

import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.capsule.entity.Video;

public interface VideoRepository extends Repository<Video, Long>, VideoQueryRepository {

    void save(Video video);
}
