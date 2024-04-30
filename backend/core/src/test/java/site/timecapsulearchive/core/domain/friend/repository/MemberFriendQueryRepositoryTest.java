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
import site.timecapsulearchive.core.domain.friend.data.dto.SearchTagFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.member.entity.Member;

@TestConstructor(autowireMode = AutowireMode.ALL)
class MemberFriendQueryRepositoryTest extends RepositoryTest {

    private static final int MAX_FRIEND_ID = 21;
    private static final int MAX_MEMBER_ID = 31;

    private final List<byte[]> hashedNotMemberPhones = new ArrayList<>();
    private final List<byte[]> hashedFriendPhones = new ArrayList<>();
    private final List<byte[]> hashedNotFriendPhones = new ArrayList<>();

    private final MemberFriendQueryRepository memberFriendQueryRepository;

    private Long ownerId;
    private Long friendId;

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

        List<Member> friends = MemberFixture.members(1, MAX_FRIEND_ID - 1);
        //owner와 친구, 친구 초대 데이터
        for (Member friend : friends) {
            entityManager.persist(friend);
            hashedFriendPhones.add(friend.getPhone_hash());

            MemberFriend memberFriend = MemberFriendFixture.memberFriend(owner, friend);
            entityManager.persist(memberFriend);

            MemberFriend friendMember = MemberFriendFixture.memberFriend(friend, owner);
            entityManager.persist(friendMember);

            //friend -> owner 친구 요청
            FriendInvite friendInvite = FriendInviteFixture.friendInvite(friend, owner);
            entityManager.persist(friendInvite);
        }

        //friend id
        friendId = friends.get(friends.size() - 1).getId();

        //owner와 친구가 아닌 멤버 데이터
        List<Member> members = MemberFixture.members(MAX_FRIEND_ID, 10);
        for (Member notFriend : members) {
            entityManager.persist(notFriend);
            hashedNotFriendPhones.add(notFriend.getPhone_hash());
        }

        //회원이 아닌 휴대전화번호 데이터
        hashedNotMemberPhones.addAll(MemberFixture.getPhoneBytesList(MAX_MEMBER_ID + 20, 10));
    }

    @ParameterizedTest
    @ValueSource(ints = {20, 15, 10, 5})
    void 특정_사용자로_친구_목록_조회하면_친구_목록_리스트가_나온다(int size) {
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
    void 친구_요청만_보낸_사용자의_친구_목록_조회하면_빈_리스트가_나온다() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendsSlice(
            friendId,
            size,
            now
        );

        //then
        assertThat(slice.getContent().size()).isEqualTo(0);
    }

    @Test
    void 유효하지_않은_시간으로_사용자의_친구_목록_조회하면_빈_리스트가_나온다() {
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
        assertThat(slice.getContent().size()).isEqualTo(0);
    }

    @Test
    void 친구가_없는_사용자로_친구_목록_조회하면_빈_리스트가_나온다() {
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
        assertThat(slice.getContent().size()).isEqualTo(0);
    }

    @ParameterizedTest
    @ValueSource(ints = {20, 15, 10, 5})
    void 특정_사용자로_친구_요청_목록_조회하면_친구_요청_목록_리스트가_나온다(int size) {
        //given
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendRequestsSlice(
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
    void 유효하지_않은_시간으로_사용자의_친구_요청_목록_조회하면_빈_리스트가_나온다() {
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
        assertThat(slice.getContent().size()).isEqualTo(0);
    }

    @Test
    void 친구가_없는_사용자로_친구_요청_목록_조회하면_빈_리스트가_나온다() {
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
        assertThat(slice.getContent().size()).isEqualTo(0);
    }

    @Test
    void 앱_사용자의_전화번호가_아닌_경우_주소록_기반_사용자_리스트_조회하면_빈_리스트가_나온다() {
        //given
        //when
        List<SearchFriendSummaryDto> friends = memberFriendQueryRepository.findFriendsByPhone(
            ownerId,
            hashedNotMemberPhones);

        //then
        assertThat(friends.size()).isSameAs(0);
    }

    @Test
    void 친구인_사용자로_주소록_기반_사용자_리스트_조회하면_친구인_앱_사용자_리스트가_나온다() {
        //given
        //when
        List<SearchFriendSummaryDto> friends = memberFriendQueryRepository.findFriendsByPhone(
            ownerId,
            hashedFriendPhones);

        //then
        assertSoftly(softly -> {
            softly.assertThat(friends.size()).isSameAs(MAX_FRIEND_ID - 1);
            softly.assertThat(friends).allMatch(friend -> friend.isFriend() == Boolean.TRUE);
        });
    }

    @Test
    void 친구가_아닌_사용자로_주소록_기반_사용자_리스트_조회하면_앱_사용자_리스트가_나온다() {
        //given
        //when
        List<SearchFriendSummaryDto> friends = memberFriendQueryRepository.findFriendsByPhone(
            ownerId,
            hashedNotFriendPhones);

        //then
        assertSoftly(softly -> {
            assertThat(friends.size()).isSameAs(MAX_MEMBER_ID - MAX_FRIEND_ID);
            softly.assertThat(friends).allMatch(friend -> friend.isFriend() == Boolean.FALSE);
        });
    }

    @Test
    void 빈_전화번호_목록으로_주소록_기반_사용자_리스트_조회하면_빈_리스트가_나온다() {
        //given
        List<byte[]> phoneHashes = Collections.emptyList();

        //when
        List<SearchFriendSummaryDto> friends = memberFriendQueryRepository.findFriendsByPhone(
            ownerId,
            phoneHashes);

        //then
        assertThat(friends.size()).isSameAs(0);
    }

    @ParameterizedTest
    @ValueSource(ints = {20, 15, 10, 5})
    void 사용자_아이디와_친구_태그로_친구관계를_조회하면_친구인_경우_True를_반환한다(int friendId) {
        //given
        SearchTagFriendSummaryDto dto = memberFriendQueryRepository.findFriendsByTag(
            ownerId, friendId + "testTag").orElseThrow();

        //when
        Boolean isFriend = dto.isFriend();

        //then
        assertThat(isFriend).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {30, 28, 25, 21})
    void 사용자_아이디와_친구_태그로_친구관계를_조회하면_친구가_아닌_경우_False를_반환한다(int friendId) {
        //given
        SearchTagFriendSummaryDto dto = memberFriendQueryRepository.findFriendsByTag(
            ownerId, friendId + "testTag").orElseThrow();

        //when
        Boolean isFriend = dto.isFriend();

        //then
        assertThat(isFriend).isFalse();
    }
}