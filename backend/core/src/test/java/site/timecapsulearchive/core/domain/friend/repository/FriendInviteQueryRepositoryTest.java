package site.timecapsulearchive.core.domain.friend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.Slice;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.common.fixture.domain.FriendInviteFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;
import site.timecapsulearchive.core.domain.friend.repository.friend_invite.FriendInviteQueryRepository;
import site.timecapsulearchive.core.domain.friend.repository.friend_invite.FriendInviteQueryRepositoryImpl;
import site.timecapsulearchive.core.domain.member.entity.Member;

@TestConstructor(autowireMode = AutowireMode.ALL)
class FriendInviteQueryRepositoryTest extends RepositoryTest {

    private static final int MAX_COUNT = 40;
    private static final Long BULK_FRIEND_INVITE_MEMBER_START_ID = 2L;
    private static final Long FRIEND_RECEPTION_INVITE_MEMBER_START_ID =
        BULK_FRIEND_INVITE_MEMBER_START_ID + MAX_COUNT;
    private static final Long FRIEND_SENDING_INVITE_MEMBER_START_ID =
        FRIEND_RECEPTION_INVITE_MEMBER_START_ID + MAX_COUNT;
    private static final Long NOT_FRIEND_INVITE_START_ID =
        FRIEND_SENDING_INVITE_MEMBER_START_ID + MAX_COUNT;

    private final FriendInviteQueryRepository friendInviteQueryRepository;
    private final EntityManager entityManager;
    private final List<Member> friends = new ArrayList<>();

    private Long bulkOwnerId;
    private Long ownerId;
    private Long ownerInviteReceptionStartId;
    private Long ownerInviteSendingStartId;

    FriendInviteQueryRepositoryTest(EntityManager entityManager, JdbcTemplate jdbcTemplate,
        JPAQueryFactory jpaQueryFactory) {
        this.entityManager = entityManager;
        this.friendInviteQueryRepository = new FriendInviteQueryRepositoryImpl(jdbcTemplate,
            jpaQueryFactory);
    }

    @BeforeEach
    void setup() {
        // 벌크 저장 시 owner 멤버 데이터
        Member bulkOwner = MemberFixture.member(0);
        entityManager.persist(bulkOwner);
        bulkOwnerId = bulkOwner.getId();

        // 벌크 저장 시 owner 친구 데이터
        friends.addAll(MemberFixture.members(2, BULK_FRIEND_INVITE_MEMBER_START_ID.intValue()));
        friends.forEach(entityManager::persist);

        // 친구 초대 owner 멤버 데이터
        Member owner = MemberFixture.member(1);
        entityManager.persist(owner);
        ownerId = owner.getId();

        // owner에게 친구 요청만 받은 멤버 데이터
        List<Member> receptionInviteToOwnerMembers = MemberFixture.members(
            FRIEND_RECEPTION_INVITE_MEMBER_START_ID.intValue(), MAX_COUNT);
        for (Member member : receptionInviteToOwnerMembers) {
            entityManager.persist(member);

            FriendInvite receptionInvite = FriendInviteFixture.friendInvite(owner, member);
            entityManager.persist(receptionInvite);
        }
        ownerInviteReceptionStartId = receptionInviteToOwnerMembers.get(0).getId();

        // owner에게 친구 요청만 보낸 멤버 데이터
        List<Member> sendingInviteToOwnerMembers = MemberFixture.members(
            FRIEND_SENDING_INVITE_MEMBER_START_ID.intValue(), MAX_COUNT);
        for (Member member : sendingInviteToOwnerMembers) {
            entityManager.persist(member);

            FriendInvite sendingInvite = FriendInviteFixture.friendInvite(member, owner);
            entityManager.persist(sendingInvite);
        }
        ownerInviteSendingStartId = sendingInviteToOwnerMembers.get(0).getId();

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void 대량의_친구_초대를_저장하면_조회하면_친구_초대를_볼_수_있다() {
        //given
        List<Long> friendIds = friends.stream()
            .map(Member::getId)
            .toList();

        //when
        friendInviteQueryRepository.bulkSave(bulkOwnerId, friendIds);

        //then
        List<FriendInvite> friendInvites = getFriendInvites(entityManager, bulkOwnerId);
        assertThat(friendInvites.size()).isEqualTo(friendIds.size());
    }

    private List<FriendInvite> getFriendInvites(EntityManager entityManager, Long ownerId) {
        Query query = entityManager.createQuery(
            "select f from FriendInvite f where owner.id = :ownerId", FriendInvite.class);
        query.setParameter("ownerId", ownerId);
        return query.getResultList();
    }


    @ValueSource(ints = {2, 7, 10, 5})
    @ParameterizedTest
    void 사용자가_보낸_친구_요청_받은_목록을_조회하면_보낸_친구_요청목록이_나온다(int size) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(3);

        Slice<FriendSummaryDto> slice = friendInviteQueryRepository.findFriendSendingInvitesSlice(
            ownerId,
            size,
            now
        );

        assertThat(slice.getContent()).isNotEmpty();
        assertThat(slice.getContent()).allMatch(dto -> dto.id() >= ownerInviteReceptionStartId
            && dto.id() < ownerInviteReceptionStartId + MAX_COUNT);
    }

    @Test
    void 사용자가_첫_페이지_이후의_친구요청_보낸_목록을_조회하면_다음_페이지의_보낸_친구_요청목록이_나온다() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(3);
        Slice<FriendSummaryDto> firstSlice = friendInviteQueryRepository.findFriendSendingInvitesSlice(
            ownerId, size, now);

        //when
        FriendSummaryDto dto = firstSlice.getContent().get(firstSlice.getNumberOfElements() - 1);
        Slice<FriendSummaryDto> nextSlice = friendInviteQueryRepository.findFriendSendingInvitesSlice(
            ownerId, size, dto.createdAt());

        //then
        assertThat(nextSlice.getNumberOfElements()).isPositive();
    }

    @Test
    void 사용자가_유효하지_않은_시간으로_사용자의_친구_요청_보낸_목록_조회하면_빈_리스트가_나온다() {
        //given
        int size = 10;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(5);

        //when
        Slice<FriendSummaryDto> slice = friendInviteQueryRepository.findFriendSendingInvitesSlice(
            ownerId,
            size,
            now
        );

        //then
        assertThat(slice).isEmpty();
    }

    @Test
    void 친구_요청을_보내지_않은_사용자가_친구_요청_보낸_목록_조회하면_빈_리스트가_나온다() {
        //given
        int size = 10;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(5);

        //when
        Slice<FriendSummaryDto> slice = friendInviteQueryRepository.findFriendSendingInvitesSlice(
            NOT_FRIEND_INVITE_START_ID,
            size,
            now
        );

        //then
        assertThat(slice).isEmpty();
    }

    @ValueSource(ints = {2, 7, 10, 5})
    @ParameterizedTest
    void 사용자가_받은_친구_요청_받은_목록을_조회하면_받은_친구_요청목록이_나온다(int size) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(3);

        Slice<FriendSummaryDto> slice = friendInviteQueryRepository.findFriendReceptionInvitesSlice(
            ownerId,
            size,
            now
        );

        assertThat(slice.getContent()).isNotEmpty();
        assertThat(slice.getContent()).allMatch(dto -> dto.id() >= ownerInviteSendingStartId
            && dto.id() < ownerInviteSendingStartId + MAX_COUNT);
    }

    @Test
    void 사용자가_첫_페이지_이후의_친구요청_받은_목록을_조회하면_다음_페이지의_받은_친구_요청목록이_나온다() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(3);
        Slice<FriendSummaryDto> firstSlice = friendInviteQueryRepository.findFriendReceptionInvitesSlice(
            ownerId, size, now);

        //when
        FriendSummaryDto dto = firstSlice.getContent().get(firstSlice.getNumberOfElements() - 1);
        Slice<FriendSummaryDto> nextSlice = friendInviteQueryRepository.findFriendReceptionInvitesSlice(
            ownerId, size, dto.createdAt());

        //then
        assertThat(nextSlice.getNumberOfElements()).isPositive();
    }

    @Test
    void 사용자가_유효하지_않은_시간으로_사용자의_친구_요청_받은_목록_조회하면_빈_리스트가_나온다() {
        //given
        int size = 10;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(5);

        //when
        Slice<FriendSummaryDto> slice = friendInviteQueryRepository.findFriendReceptionInvitesSlice(
            ownerId,
            size,
            now
        );

        //then
        assertThat(slice).isEmpty();
    }

    @Test
    void 친구_요청을_받지_않은_사용자가_친구_요청_받은_목록_조회하면_빈_리스트가_나온다() {
        //given
        int size = 10;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(5);

        //when
        Slice<FriendSummaryDto> slice = friendInviteQueryRepository.findFriendReceptionInvitesSlice(
            NOT_FRIEND_INVITE_START_ID,
            size,
            now
        );

        //then
        assertThat(slice).isEmpty();
    }
}