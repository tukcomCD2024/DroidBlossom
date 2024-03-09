package site.timecapsulearchive.core.domain.friend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.friend.exception.FriendNotFoundException;
import site.timecapsulearchive.core.domain.friend.repository.MemberFriendRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendService {

    private final MemberFriendRepository memberFriendRepository;

    @Transactional
    public void deleteFriend(final Long memberId, final Long friendId) {
        MemberFriend memberFriend = memberFriendRepository.findMemberFriendByOwnerIdAndFriendId(
                memberId, friendId)
            .orElseThrow(FriendNotFoundException::new);

        memberFriendRepository.delete(memberFriend);
    }
}
