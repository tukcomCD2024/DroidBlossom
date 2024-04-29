package site.timecapsulearchive.core.domain.capsule.generic_capsule.repository;

import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.capsule.entity.Video;

public interface VideoRepository extends Repository<Video, Long> {

    void save(Video video);
}
