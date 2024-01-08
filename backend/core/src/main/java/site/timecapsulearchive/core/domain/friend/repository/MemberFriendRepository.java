package site.timecapsulearchive.core.domain.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;

public interface MemberFriendRepository extends JpaRepository<MemberFriend, Long> {

}
