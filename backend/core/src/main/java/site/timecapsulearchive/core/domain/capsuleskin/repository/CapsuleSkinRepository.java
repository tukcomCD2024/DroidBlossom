package site.timecapsulearchive.core.domain.capsuleskin.repository;

import java.util.Optional;
import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;

public interface CapsuleSkinRepository extends Repository<CapsuleSkin, Long> {

    Optional<CapsuleSkin> findCapsuleSkinById(Long capsuleSkinId);
}
