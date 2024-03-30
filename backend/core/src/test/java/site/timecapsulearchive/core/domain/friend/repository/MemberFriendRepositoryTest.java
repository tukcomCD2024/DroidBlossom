package site.timecapsulearchive.core.domain.friend.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.common.data.MemberTestDataRepository;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.SocialType;

@FlywayTest
@TestConstructor(autowireMode = AutowireMode.ALL)
class MemberFriendRepositoryTest extends RepositoryTest {

    private final MemberFriendRepository memberFriendRepository;
    private final MemberTestDataRepository memberTestDataRepository = new MemberTestDataRepository();
    
    private Member owner;
    private Member friend;

    MemberFriendRepositoryTest(MemberFriendRepository repository) {
        this.memberFriendRepository = repository;
    }

    @Transactional
    @BeforeEach
    void setup(@Autowired EntityManager entityManager) {
        owner = memberTestDataRepository.insertAndGetMember(entityManager, 0);
        friend = memberTestDataRepository.insertAndGetMember(entityManager, 1);

        memberTestDataRepository.insertMemberFriend(entityManager, owner, friend);
        memberTestDataRepository.insertMemberFriend(entityManager, friend, owner);
    }

    @Test
    void 사용자_아이디와_친구_아이디로_친구관계를_조회하여_친구관계를_확인한다() {
        //given
        List<MemberFriend> memberFriend = memberFriendRepository.findMemberFriendByOwnerIdAndFriendId(
            owner.getId(), friend.getId());

        //when
        Long actualFriendId = memberFriend.get(0).getFriend().getId();
        Long actualOwnerId = memberFriend.get(1).getFriend().getId();

        //when
        assertSoftly(softly -> {
            softly.assertThat(memberFriend.size()).isEqualTo(2);
            softly.assertThat(actualFriendId).isEqualTo(friend.getId());
            softly.assertThat(actualOwnerId).isEqualTo(owner.getId());
        });
    }

    @Test
    void 유효하지_않는_친구_아이디로_친구관계를_조회하면_빈_리스트를_반환한다() {
        //given
        Long friendId = -1L;

        //when
        List<MemberFriend> memberFriend = memberFriendRepository.findMemberFriendByOwnerIdAndFriendId(
            owner.getId(), friendId);

        //when
        assertThat(memberFriend).isEmpty();
    }


}
