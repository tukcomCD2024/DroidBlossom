package site.timecapsulearchive.core.domain.capsule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.core.domain.capsule.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {

}
