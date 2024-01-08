package site.timecapsulearchive.core.domain.history.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.core.domain.history.entity.HistoryImage;

public interface HistoryImageRepository extends JpaRepository<HistoryImage, Long> {

}
