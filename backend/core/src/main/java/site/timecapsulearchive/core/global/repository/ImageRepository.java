package site.timecapsulearchive.core.global.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.core.global.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
