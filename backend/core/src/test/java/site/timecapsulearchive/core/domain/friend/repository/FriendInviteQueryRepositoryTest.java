package site.timecapsulearchive.core.domain.friend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;
import site.timecapsulearchive.core.domain.friend.repository.friend_invite.FriendInviteQueryRepository;
import site.timecapsulearchive.core.domain.friend.repository.friend_invite.FriendInviteQueryRepositoryImpl;
import site.timecapsulearchive.core.domain.member.entity.Member;

@TestConstructor(autowireMode = AutowireMode.ALL)
class FriendInviteQueryRepositoryTest extends RepositoryTest {

    private final FriendInviteQueryRepository friendInviteQueryRepository;
    private final EntityManager entityManager;
    private final List<Member> friends = new ArrayList<>();

    private Member owner;

    FriendInviteQueryRepositoryTest(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
        this.friendInviteQueryRepository = new FriendInviteQueryRepositoryImpl(jdbcTemplate);
    }

    @BeforeEach
    void setup() {
        owner = MemberFixture.member(0);
        entityManager.persist(owner);

        friends.addAll(MemberFixture.members(1, 11));
        friends.forEach(entityManager::persist);
    }

    @Test
    void 대량의_친구_초대를_저장하면_조회하면_친구_초대를_볼_수_있다() {
        //given
        Long ownerId = owner.getId();
        List<Long> friendIds = friends.stream()
            .map(Member::getId)
            .toList();

        //when
        friendInviteQueryRepository.bulkSave(ownerId, friendIds);

        //then
        List<FriendInvite> friendInvites = getFriendInvites(entityManager, ownerId);
        assertThat(friendInvites.size()).isEqualTo(friendIds.size());
    }

    private List<FriendInvite> getFriendInvites(EntityManager entityManager, Long ownerId) {
        Query query = entityManager.createQuery(
            "select f from FriendInvite f where owner.id = :ownerId", FriendInvite.class);
        query.setParameter("ownerId", ownerId);
        return query.getResultList();
    }
}