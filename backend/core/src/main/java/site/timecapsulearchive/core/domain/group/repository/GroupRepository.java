package site.timecapsulearchive.core.domain.group.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.group.entity.Group;

public interface GroupRepository extends Repository<Group, Long>, GroupQueryRepository {

    void save(Group group);

    Optional<Group> findGroupById(Long groupId);

    void delete(Group group);

    @Query("select g from Group g join MemberGroup mg where mg.member.id = :memberId and mg.isOwner = true")
    List<Group> findAllOwnerGroupsByMemberId(Long memberId);
}