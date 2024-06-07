package site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.video;

import java.util.List;
import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.capsule.entity.Video;

public interface VideoQueryRepository {

    void bulkSave(final List<Video> videos);
}
