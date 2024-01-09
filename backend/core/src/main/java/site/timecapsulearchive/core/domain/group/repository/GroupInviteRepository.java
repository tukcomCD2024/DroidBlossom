package site.timecapsulearchive.core.domain.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.core.domain.group.entity.GroupInvite;

public interface GroupInviteRepository extends JpaRepository<GroupInvite, Long> {

}

