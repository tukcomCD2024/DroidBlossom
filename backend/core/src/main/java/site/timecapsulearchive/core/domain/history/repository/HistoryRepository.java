package site.timecapsulearchive.core.domain.history.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.core.domain.history.entity.History;

public interface HistoryRepository extends JpaRepository<History, Long> {

}