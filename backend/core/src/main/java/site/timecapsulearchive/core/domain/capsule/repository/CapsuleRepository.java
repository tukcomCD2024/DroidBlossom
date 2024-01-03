package site.timecapsulearchive.core.domain.capsule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;

public interface CapsuleRepository extends JpaRepository<Capsule, Long> {

}
