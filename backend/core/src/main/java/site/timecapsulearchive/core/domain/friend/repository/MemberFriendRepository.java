package site.timecapsulearchive.core.domain.friend.repository;

import java.util.Optional;
import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;

public interface MemberFriendRepository extends Repository<MemberFriend, Long> {

    Optional<MemberFriend> findMemberFriendByOwnerIdAndFriendId(Long ownerId, Long friendId);

    void delete(MemberFriend memberFriend);
}
