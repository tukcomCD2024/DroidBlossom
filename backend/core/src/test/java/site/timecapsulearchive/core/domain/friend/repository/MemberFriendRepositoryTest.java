package site.timecapsulearchive.core.domain.friend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.SocialType;

@FlywayTest
@TestConstructor(autowireMode = AutowireMode.ALL)
class MemberFriendRepositoryTest extends RepositoryTest {

    private final MemberFriendRepository memberFriendRepository;
    private Long ownerId;
    private Long friendId;


    MemberFriendRepositoryTest(MemberFriendRepository repository) {
        this.memberFriendRepository = repository;
    }

    @Transactional
    @BeforeEach
    void setup(@Autowired EntityManager entityManager) {
        Member owner = getMember(0);
        entityManager.persist(owner);
        ownerId = owner.getId();

        Member friend = getMember(1);
        entityManager.persist(friend);
        friendId = friend.getId();

        MemberFriend memberFriend = MemberFriend.builder()
            .owner(owner)
            .friend(friend)
            .build();
        entityManager.persist(memberFriend);
    }

    private Member getMember(int count) {
        return Member.builder()
            .socialType(SocialType.GOOGLE)
            .nickname(count + "testNickname")
            .email(count + "test@google.com")
            .authId(count + "test")
            .profileUrl(count + "test.com")
            .tag(count + "testTag")
            .build();
    }

    @Test
    void 사용자_아이디와_친구_아이디로_친구관계를_조회하여_친구관계를_확인한다() {
        //given
        Optional<MemberFriend> memberFriend = memberFriendRepository.findMemberFriendByOwnerIdAndFriendId(
            ownerId, friendId);

        //when
        Long actualFriendId = memberFriend.get().getFriend().getId();

        //then
        assertThat(actualFriendId).isEqualTo(friendId);
    }


}
