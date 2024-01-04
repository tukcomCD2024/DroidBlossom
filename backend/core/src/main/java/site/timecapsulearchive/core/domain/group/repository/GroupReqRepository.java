package site.timecapsulearchive.core.domain.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.core.domain.group.entity.GroupRequest;

public interface GroupReqRepository extends JpaRepository<GroupRequest, Long> {

}

