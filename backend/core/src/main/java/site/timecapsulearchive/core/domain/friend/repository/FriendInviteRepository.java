package site.timecapsulearchive.core.domain.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;

public interface FriendInviteRepository extends JpaRepository<FriendInvite, Long> {

}

