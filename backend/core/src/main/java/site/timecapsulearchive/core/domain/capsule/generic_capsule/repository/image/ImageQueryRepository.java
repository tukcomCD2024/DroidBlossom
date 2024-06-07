package site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.image;

import java.util.List;
import java.util.Optional;
import site.timecapsulearchive.core.domain.capsule.entity.Image;

public interface ImageQueryRepository {

    void bulkSave(final List<Image> images);

    Optional<Long> deleteImage(final Long imageId);

}
