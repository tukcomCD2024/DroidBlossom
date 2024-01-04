package site.timecapsulearchive.core.domain.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.core.domain.friend.entity.FriendRequest;

public interface FriendReqRepository extends JpaRepository<FriendRequest, Long> {

}

