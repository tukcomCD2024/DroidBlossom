package site.timecapsulearchive.core.domain.friend.repository;

import java.util.Optional;
import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;

public interface FriendInviteRepository extends Repository<FriendInvite, Long> {

    void save(FriendInvite friendInvite);

    Optional<FriendInvite> findFriendInviteByOwnerIdAndFriendId(Long memberId, Long friendId);

    void deleteFriendInviteById(Long id);

    void delete(FriendInvite friendInvite);
}

