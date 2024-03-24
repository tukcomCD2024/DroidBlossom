package site.timecapsulearchive.core.domain.friend.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import org.flywaydb.test.annotation.FlywayTest;
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
import site.timecapsulearchive.core.domain.friend.data.dto.FriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;
import site.timecapsulearchive.core.domain.friend.entity.FriendStatus;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.SocialType;

@FlywayTest
@TestConstructor(autowireMode = AutowireMode.ALL)
class MemberFriendQueryRepositoryTest extends RepositoryTest {

    private final MemberFriendQueryRepository memberFriendQueryRepository;
    private Long memberId;

    MemberFriendQueryRepositoryTest(EntityManager entityManager) {
        this.memberFriendQueryRepository = new MemberFriendQueryRepository(
            new JPAQueryFactory(entityManager));
    }

    @Transactional
    @BeforeEach
    void setup(@Autowired EntityManager entityManager) {
        Member owner = getMember(0);
        entityManager.persist(owner);
        memberId = owner.getId();

        for (int i = 1; i < 21; i++) {
            Member friend = getMember(i);
            entityManager.persist(friend);

            MemberFriend memberFriend = MemberFriend.builder()
                .friend(friend)
                .owner(owner)
                .build();
            entityManager.persist(memberFriend);

            FriendInvite friendInvite = FriendInvite.builder()
                .friend(friend)
                .owner(owner)
                .friendStatus(FriendStatus.PENDING)
                .build();
            entityManager.persist(friendInvite);
        }
    }

    private Member getMember(int count) {
        return Member.builder()
            .socialType(SocialType.GOOGLE)
            .email(count + "test@google.com")
            .authId(count + "test")
            .profileUrl(count + "test.com")
            .build();
    }

    @ParameterizedTest
    @ValueSource(ints = {20, 15, 10, 5})
    void 사용자의_친구_목록_조회_테스트(int size) {
        //given
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendsSlice(
            memberId,
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
    void 유효하지_않은_시간으로_사용자의_친구_목록_조회_테스트() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendsSlice(
            memberId,
            size,
            now
        );

        //then
        assertThat(slice.getContent().size()).isEqualTo(0);
    }

    @Test
    void 친구가_없는_사용자의_친구_목록_조회_테스트() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendsSlice(
            memberId,
            size,
            now
        );

        //then
        assertThat(slice.getContent().size()).isEqualTo(0);
    }

    @ParameterizedTest
    @ValueSource(ints = {20, 15, 10, 5})
    void 사용자의_친구_요청_목록_조회_테스트(int size) {
        //given
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendRequestsSlice(
            memberId,
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
    void 유효하지_않은_시간으로_사용자의_친구_요청_목록_조회_테스트() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendRequestsSlice(
            memberId,
            size,
            now
        );

        //then
        assertThat(slice.getContent().size()).isEqualTo(0);
    }

    @Test
    void 친구가_없는_사용자의_친구_요청_목록_조회_테스트() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendRequestsSlice(
            memberId,
            size,
            now
        );

        //then
        assertThat(slice.getContent().size()).isEqualTo(0);
    }
}