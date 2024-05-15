package site.timecapsulearchive.core.domain.group_member.repository.memberGroupRepository;

import java.util.List;
import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.group_member.entity.MemberGroup;

public interface MemberGroupRepository extends Repository<MemberGroup, Long>,
    MemberGroupQueryRepository {

    void save(MemberGroup memberGroup);

    List<MemberGroup> findMemberGroupsByGroupId(Long groupId);

    void delete(MemberGroup memberGroup);
}
