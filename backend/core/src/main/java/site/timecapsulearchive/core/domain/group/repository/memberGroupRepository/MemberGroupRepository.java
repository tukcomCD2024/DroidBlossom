package site.timecapsulearchive.core.domain.group.repository.memberGroupRepository;

import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.group.entity.MemberGroup;

public interface MemberGroupRepository extends Repository<MemberGroup, Long>,
    MemberGroupQueryRepository {

    void save(MemberGroup memberGroup);
}
