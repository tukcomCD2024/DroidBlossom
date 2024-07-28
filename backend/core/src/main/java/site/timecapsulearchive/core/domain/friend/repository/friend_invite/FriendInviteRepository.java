package site.timecapsulearchive.core.domain.friend.repository.friend_invite;

import java.time.ZonedDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;

public interface FriendInviteRepository extends Repository<FriendInvite, Long>,
    FriendInviteQueryRepository {

    void save(FriendInvite friendInvite);

    void delete(FriendInvite friendInvite);

    Optional<FriendInvite> findFriendSendingInviteForUpdateByOwnerIdAndFriendId(Long memberId,
        Long friendId);

    @Query(value = """
        select fi
        from FriendInvite fi
        join fetch fi.owner
        join fetch fi.friend
        where fi.owner.id =:ownerId and fi.friend.id =:friendId
        """)
    Optional<FriendInvite> findFriendReceivingInviteForUpdateByOwnerIdAndFriendId(
        @Param(value = "ownerId") Long ownerId,
        @Param(value = "friendId") Long friendId
    );

    @Query("UPDATE FriendInvite fi SET fi.deletedAt = :deletedAt WHERE fi.friend.id = :memberId or fi.owner.id = :memberId")
    @Modifying
    void deleteByMemberId(
        @Param("memberId") Long memberId,
        @Param("deletedAt") ZonedDateTime deletedAt
    );
}

