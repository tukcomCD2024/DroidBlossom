package site.timecapsulearchive.core.domain.member_group.repository.member_group_repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import site.timecapsulearchive.core.domain.member_group.entity.MemberGroup;

public interface MemberGroupRepository extends Repository<MemberGroup, Long>,
    MemberGroupQueryRepository {

    void save(MemberGroup memberGroup);

    List<MemberGroup> findMemberGroupsByGroupId(Long groupId);

    void delete(MemberGroup memberGroup);

    Optional<MemberGroup> findMemberGroupByMemberIdAndGroupId(Long memberId, Long groupId);

    @Query("UPDATE MemberGroup mg SET mg.deletedAt = :deletedAt WHERE mg.member.id = :memberId")
    @Modifying
    void deleteByMemberId(
        @Param("memberId") Long memberId,
        @Param("deletedAt") ZonedDateTime deletedAt
    );

    @Query("UPDATE MemberGroup mg SET mg.deletedAt = :deletedAt WHERE mg.group.id in :groupIds")
    @Modifying
    void deleteByGroupIds(
        @Param("groupIds") List<Long> groupIds,
        @Param("deletedAt") ZonedDateTime deletedAt
    );
}
