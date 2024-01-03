package site.timecapsulearchive.core.global.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.core.global.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {

}
