package site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.image;

import java.util.Optional;
import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.capsule.entity.Image;

public interface ImageRepository extends Repository<Image, Long>, ImageQueryRepository {

    void save(Image newImage);

    Optional<Image> findById(Long imageId);
}
