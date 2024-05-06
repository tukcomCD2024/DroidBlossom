package site.timecapsulearchive.core.domain.friend.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.common.fixture.domain.FriendInviteFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFriendFixture;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDtoByTag;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.member.entity.Member;

@TestConstructor(autowireMode = AutowireMode.ALL)
class MemberFriendQueryRepositoryTest extends RepositoryTest {

    private static final int MAX_COUNT = 10;
    private static final Long FRIEND_START_ID = 1L;
    private static final Long FRIEND_ID_TO_INVITE_OWNER = 11L;
    private static final Long NOT_FRIEND_MEMBER_START_ID = 12L;

    private final List<byte[]> hashedNotMemberPhones = new ArrayList<>();
    private final List<byte[]> hashedFriendPhones = new ArrayList<>();
    private final List<byte[]> hashedNotFriendPhones = new ArrayList<>();

    private Long ownerId;
    private Long friendId;
    private final MemberFriendQueryRepository memberFriendQueryRepository;

    MemberFriendQueryRepositoryTest(EntityManager entityManager) {
        this.memberFriendQueryRepository = new MemberFriendQueryRepository(
            new JPAQueryFactory(entityManager));
    }

    @Transactional
    @BeforeEach
    void setup(@Autowired EntityManager entityManager) {
        Member owner = MemberFixture.member(0);
        entityManager.persist(owner);
        ownerId = owner.getId();

        // owner와 친구 관계를 맺는 데이터
        List<Member> friends = MemberFixture.members(FRIEND_START_ID.intValue(), MAX_COUNT);
        for (Member friend : friends) {
            entityManager.persist(friend);
            hashedFriendPhones.add(friend.getPhone_hash());

            MemberFriend memberFriend = MemberFriendFixture.memberFriend(owner, friend);
            entityManager.persist(memberFriend);

            MemberFriend friendMember = MemberFriendFixture.memberFriend(friend, owner);
            entityManager.persist(friendMember);
        }
        friendId = friends.get(0).getId();

        // owner에게 요청만 보낸 데이터
        Member inviteFriendToOwner = MemberFixture.member(FRIEND_ID_TO_INVITE_OWNER.intValue());
        entityManager.persist(inviteFriendToOwner);

        FriendInvite friendInvite = FriendInviteFixture.friendInvite(inviteFriendToOwner, owner);
        entityManager.persist(friendInvite);

        //owner와 친구가 아닌 멤버 데이터
        List<Member> notFriendMembers = MemberFixture.members(NOT_FRIEND_MEMBER_START_ID.intValue(),
            MAX_COUNT);
        for (Member notFriend : notFriendMembers) {
            entityManager.persist(notFriend);
            hashedNotFriendPhones.add(notFriend.getPhone_hash());
        }

        //회원이 아닌 휴대전화번호 데이터
        hashedNotMemberPhones.addAll(MemberFixture.getPhoneBytesList(23, MAX_COUNT));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 7, 10, 5})
    void 사용자가_친구_목록_조회하면_친구_관계를_맺은_사용자_리스트가_나온다(int size) {
        //given
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendsSlice(
            ownerId,
            size,
            now
        );

        //then
        assertSoftly(softly -> {
                softly.assertThat(slice.getContent().size()).isEqualTo(size);
                softly.assertThat(slice.getContent()).allMatch(dto -> dto.createdAt().isBefore(now));
                softly.assertThat(slice.getContent()).allMatch(dto -> Objects.nonNull(dto.id()));
                softly.assertThat(slice.getContent()).allMatch(dto -> !dto.profileUrl().isBlank());
                softly.assertThat(slice.getContent()).allMatch(dto -> !dto.nickname().isBlank());
            }
        );
    }

    @Test
    void 친구가_친구_목록_조회하면_친구_관계를_맺은_사용자_리스트가_나온다() {
        //given
        int size = 10;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendsSlice(
            friendId,
            size,
            now
        );

        assertThat(slice.getContent().size()).isEqualTo(1);
    }

    @Test
    void 친구_요청만_보낸_사용자가_친구_목록_조회하면_빈_리스트가_나온다() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendsSlice(
            FRIEND_ID_TO_INVITE_OWNER,
            size,
            now
        );

        //then
        assertThat(slice).isEmpty();
    }

    @Test
    void 사용자가_유효하지_않은_시간으로_사용자의_친구_목록_조회하면_빈_리스트가_나온다() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendsSlice(
            ownerId,
            size,
            now
        );

        //then
        assertThat(slice).isEmpty();
    }

    @Test
    void 친구가_없는_사용자가_친구_목록_조회하면_빈_리스트가_나온다() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendsSlice(
            NOT_FRIEND_MEMBER_START_ID,
            size,
            now
        );

        //then
        assertThat(slice).isEmpty();
    }

    @Test
    void 사용자가_유효하지_않은_시간으로_사용자의_친구_요청_목록_조회하면_빈_리스트가_나온다() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendRequestsSlice(
            ownerId,
            size,
            now
        );

        //then
        assertThat(slice).isEmpty();
    }

    @Test
    void 친구가_없는_사용자가_친구_요청_목록_조회하면_빈_리스트가_나온다() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendRequestsSlice(
            NOT_FRIEND_MEMBER_START_ID,
            size,
            now
        );

        //then
        assertThat(slice).isEmpty();
    }

    @Test
    void 사용자가_앱_사용자의_전화번호가_아닌_경우_주소록_기반_사용자_리스트_조회하면_빈_리스트가_나온다() {
        //given
        //when
        List<SearchFriendSummaryDto> friends = memberFriendQueryRepository.findFriendsByPhone(
            ownerId,
            hashedNotMemberPhones);

        //then
        assertThat(friends).isEmpty();
    }

    @Test
    void 사용자가_친구인_사용자로_주소록_기반_사용자_리스트_조회하면_친구인_앱_사용자_리스트가_나온다() {
        //given
        //when
        List<SearchFriendSummaryDto> friends = memberFriendQueryRepository.findFriendsByPhone(
            ownerId,
            hashedFriendPhones);

        //then
        assertSoftly(softly -> {
            softly.assertThat(friends.size()).isSameAs(MAX_COUNT);
            softly.assertThat(friends).allMatch(friend -> friend.isFriend() == Boolean.TRUE);
        });
    }

    @Test
    void 사용자가_친구가_아닌_사용자로_주소록_기반_사용자_리스트_조회하면_앱_사용자_리스트가_나온다() {
        //given
        //when
        List<SearchFriendSummaryDto> friends = memberFriendQueryRepository.findFriendsByPhone(
            ownerId,
            hashedNotFriendPhones);

        //then
        assertSoftly(softly -> {
            assertThat(friends.size()).isSameAs(MAX_COUNT);
            softly.assertThat(friends).allMatch(friend -> friend.isFriend() == Boolean.FALSE);
        });
    }

    @Test
    void 사용자가_빈_전화번호_목록으로_주소록_기반_사용자_리스트_조회하면_빈_리스트가_나온다() {
        //given
        List<byte[]> phoneHashes = Collections.emptyList();

        //when
        List<SearchFriendSummaryDto> friends = memberFriendQueryRepository.findFriendsByPhone(
            ownerId,
            phoneHashes);

        //then
        assertThat(friends).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 6, 10, 5})
    void 사용자가_친구_태그로_친구관계를_조회하면_친구인_경우_True를_반환한다(int friendId) {
        //given
        SearchFriendSummaryDtoByTag dto = memberFriendQueryRepository.findFriendsByTag(
            ownerId, friendId + "testTag").orElseThrow();

        //when
        Boolean isFriend = dto.isFriend();

        //then
        assertThat(isFriend).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {12, 15, 19, 21})
    void 사용자가_친구_태그로_친구관계를_조회하면_친구가_아닌_경우_False를_반환한다(int friendId) {
        //given
        SearchFriendSummaryDtoByTag dto = memberFriendQueryRepository.findFriendsByTag(
            ownerId, friendId + "testTag").orElseThrow();

        //when
        Boolean isFriend = dto.isFriend();

        //then
        assertThat(isFriend).isFalse();
    }
}