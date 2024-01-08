package site.timecapsulearchive.core.domain.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.core.domain.group.entity.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {

}