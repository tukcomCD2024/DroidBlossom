package site.timecapsulearchive.core.domain.friend.repository;

import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;

public interface FriendInviteRepository extends Repository<FriendInvite, Long> {
    void save(FriendInvite friendInvite);
}

