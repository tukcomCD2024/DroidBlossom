package site.timecapsulearchive.core.domain.group.repository;

import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.group.entity.Group;

public interface GroupRepository extends Repository<Group, Long> {

    void save(Group group);
}