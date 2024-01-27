package site.timecapsulearchive.core.domain.capsule.repository;

import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;

public interface CapsuleRepository extends Repository<Capsule, Long> {

    Capsule save(Capsule capsule);

}
