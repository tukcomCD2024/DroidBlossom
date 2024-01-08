package site.timecapsulearchive.core.domain.capsule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.core.domain.capsule.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
