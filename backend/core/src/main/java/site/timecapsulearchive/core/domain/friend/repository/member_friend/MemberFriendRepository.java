package site.timecapsulearchive.core.domain.friend.repository.member_friend;

import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;

public interface MemberFriendRepository extends Repository<MemberFriend, Long>,
    MemberFriendQueryRepository {

    @Query("SELECT mf FROM MemberFriend mf "
        + "WHERE (mf.owner.id = :memberId AND mf.friend.id = :friendId) "
        + "OR (mf.owner.id = :friendId AND mf.friend.id = :memberId)")
    List<MemberFriend> findMemberFriendByOwnerIdAndFriendId(
        @Param("memberId") Long memberId,
        @Param("friendId") Long friendId
    );

    void delete(MemberFriend memberFriend);

    void save(MemberFriend memberFriend);

    @Query("UPDATE MemberFriend mf SET mf.deletedAt = :deletedAt WHERE mf.friend.id = :memberId or mf.owner.id = :memberId")
    @Modifying
    void deleteByMemberId(
        @Param("memberId") Long memberId,
        @Param("deletedAt") ZonedDateTime deletedAt
    );
}
