package site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.image;

import java.util.List;
import site.timecapsulearchive.core.domain.capsule.entity.Image;

public interface ImageQueryRepository {

    void bulkSave(final List<Image> images);
}
