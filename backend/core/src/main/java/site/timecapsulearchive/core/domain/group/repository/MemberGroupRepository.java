package site.timecapsulearchive.core.domain.group.repository;

import java.util.List;
import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.group.entity.MemberGroup;

public interface MemberGroupRepository extends Repository<MemberGroup, Long> {

    void save(MemberGroup memberGroup);

    List<MemberGroup> findMemberGroupsByGroupId(Long groupId);

    void delete(MemberGroup mg);
}
