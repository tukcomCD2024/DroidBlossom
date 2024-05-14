package site.timecapsulearchive.core.domain.friend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;

public interface FriendInviteRepository extends Repository<FriendInvite, Long> {

    void save(FriendInvite friendInvite);

    @Query(value = "select fi "
        + "from FriendInvite fi "
        + "join fetch fi.owner "
        + "join fetch fi.friend "
        + "where (fi.owner.id =:friendId and fi.friend.id =:memberId) "
        + "or (fi.owner.id =: memberId and fi.friend.id =: friendId)")
    List<FriendInvite> findFriendInviteWithMembersByOwnerIdAndFriendId(
        @Param(value = "memberId") Long memberId,
        @Param(value = "friendId") Long friendId
    );

    Optional<FriendInvite> findFriendInviteByOwnerIdAndFriendId(Long memberId, Long targetId);

    int deleteFriendInviteByOwnerIdAndFriendId(Long memberId, Long targetId);

    void delete(FriendInvite friendInvite);
}

