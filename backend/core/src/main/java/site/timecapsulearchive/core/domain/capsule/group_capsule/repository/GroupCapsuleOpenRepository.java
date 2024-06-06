package site.timecapsulearchive.core.domain.capsule.group_capsule.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.capsule.entity.GroupCapsuleOpen;

public interface GroupCapsuleOpenRepository extends Repository<GroupCapsuleOpen, Long> {

    void save(GroupCapsuleOpen groupCapsuleOpen);

    Optional<GroupCapsuleOpen> findByMemberIdAndCapsuleId(Long memberId, Long capsuleId);

    List<GroupCapsuleOpen> findByCapsuleId(Long capsuleId);
}
