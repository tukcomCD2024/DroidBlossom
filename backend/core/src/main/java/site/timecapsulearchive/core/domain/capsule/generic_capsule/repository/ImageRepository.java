package site.timecapsulearchive.core.domain.capsule.generic_capsule.repository;

import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.capsule.entity.Image;

public interface ImageRepository extends Repository<Image, Long> {

    void save(Image newImage);
}
