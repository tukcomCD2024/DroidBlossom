package site.timecapsulearchive.core.domain.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
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
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFriendFixture;
import site.timecapsulearchive.core.common.fixture.domain.NotificationCategoryFixture;
import site.timecapsulearchive.core.common.fixture.domain.NotificationFixture;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.member.data.dto.MemberDetailDto;
import site.timecapsulearchive.core.domain.member.data.dto.MemberNotificationDto;
import site.timecapsulearchive.core.domain.member.entity.CategoryName;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.Notification;
import site.timecapsulearchive.core.domain.member.entity.NotificationCategory;

@TestConstructor(autowireMode = AutowireMode.ALL)
class MemberQueryRepositoryTest extends RepositoryTest {

    private final MemberQueryRepository memberQueryRepository;

    private Member member;
    private Member zeroNotificationMember;
    private int friendCount;

    MemberQueryRepositoryTest(EntityManager entityManager) {
        this.memberQueryRepository = new MemberQueryRepositoryImpl(
            new JPAQueryFactory(entityManager));
    }

    @Transactional
    @BeforeEach
    void setup(@Autowired EntityManager entityManager) {
        member = MemberFixture.member(0);
        entityManager.persist(member);

        zeroNotificationMember = MemberFixture.member(1);
        entityManager.persist(zeroNotificationMember);

        NotificationCategory notificationCategory = NotificationCategoryFixture.notificationCategory(
            CategoryName.CAPSULE_SKIN);
        entityManager.persist(notificationCategory);

        for (int count = 0; count < 20; count++) {
            Notification notification = NotificationFixture.notification(member,
                notificationCategory);
            entityManager.persist(notification);
        }

        for (int count = 2; count < 22; count++) {
            Member friend = MemberFixture.member(count);
            entityManager.persist(friend);

            MemberFriend memberFriend = MemberFriendFixture.memberFriend(member, friend);
            MemberFriend friendMember = MemberFriendFixture.memberFriend(friend, member);

            entityManager.persist(friendMember);
            entityManager.persist(memberFriend);

            friendCount += 1;
        }
    }

    @Test
    void 중복_이메일로_중복_체크하면_True가_반환된다() {
        //given
        String duplicatedEmail = "1test@google.com";

        //when
        Boolean isDuplicated = memberQueryRepository.checkEmailDuplication(duplicatedEmail);

        //then
        assertThat(isDuplicated).isTrue();
    }

    @Test
    void 고유한_이메일로_중복_체크_테스트하면_False가_반환된다() {
        //given
        String uniqueEmail = "unique@google.com";

        //when
        Boolean isDuplicated = memberQueryRepository.checkEmailDuplication(uniqueEmail);

        //then
        assertThat(isDuplicated).isFalse();
    }

    @Test
    void 특정_사용자로_알림_목록을_조회하면_알림_리스트가_반환된다() {
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now().plusMinutes(1);

        Slice<MemberNotificationDto> slice = memberQueryRepository.findNotificationSliceByMemberId(
            member.getId(), size, now);

        assertThat(slice.getContent()).allMatch(
            notification -> notification.createdAt().isBefore(now));
    }

    @ParameterizedTest
    @ValueSource(ints = {20, 15, 10, 5, 1})
    void 원하는_크기로_알림_목록을_조회하면_크기에_맞는_알림_리스트가_반환된다(int size) {
        ZonedDateTime now = ZonedDateTime.now().plusMinutes(1);

        Slice<MemberNotificationDto> slice = memberQueryRepository.findNotificationSliceByMemberId(
            member.getId(), size, now);

        assertThat(slice.getContent().size()).isEqualTo(size);
    }

    @Test
    void 유효하지_않은_시간으로_알림_목록을_조회하면_빈_알림_리스트가_반환된다() {
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now().minusDays(5);

        Slice<MemberNotificationDto> slice = memberQueryRepository.findNotificationSliceByMemberId(
            member.getId(), size, now);

        assertThat(slice.getContent().size()).isEqualTo(0);
    }

    @Test
    void 알림이_존재하지_않는_사용자로_알림_목록을_조회하면_빈_알림_리스트가_반환된다() {
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now().plusMinutes(1);

        Slice<MemberNotificationDto> slice = memberQueryRepository.findNotificationSliceByMemberId(
            zeroNotificationMember.getId(), size, now);

        assertThat(slice.getContent().size()).isEqualTo(0);
    }

    @Test
    void 회원의_아이디로_회원의_상세정보를_조회하면_회원의_상세정보가_반환된다() {
        //given
        //when
        MemberDetailDto detailDto = memberQueryRepository.findMemberDetailResponseDtoById(
                member.getId())
            .orElseThrow();

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(detailDto.tag()).isNotBlank();
            softly.assertThat(detailDto.nickname()).isNotBlank();
            softly.assertThat(detailDto.profileUrl()).isNotBlank();
            softly.assertThat(detailDto.friendCount()).isNotNull();
            softly.assertThat(detailDto.groupCount()).isNotNull();
        });
    }

    @Test
    void 존재하지_않는_회원의_아이디로_회원의_상세정보를_조회하면_빈_값이_반환된다() {
        //given
        Long notExistMemberId = 999999L;

        //when
        Optional<MemberDetailDto> detailDto = memberQueryRepository.findMemberDetailResponseDtoById(
            notExistMemberId);

        //then
        assertThat(detailDto.isEmpty()).isTrue();
    }

    @Test
    void 친구가_존재하는_회원의_아이디로_회원의_상세정보를_조회하면_회원의_친구수가_반환된다() {
        //given
        //when
        MemberDetailDto detailDto = memberQueryRepository.findMemberDetailResponseDtoById(
                member.getId())
            .orElseThrow();

        //then
        assertThat(detailDto.friendCount()).isEqualTo(friendCount);
    }

    @Test
    void 친구가_없는_회원의_아이디로_회원의_상세정보를_조회하면_회원의_친구수인_0이_반환된다() {
        //given
        //when
        MemberDetailDto detailDto = memberQueryRepository.findMemberDetailResponseDtoById(
                zeroNotificationMember.getId())
            .orElseThrow();

        //then
        assertThat(detailDto.friendCount()).isEqualTo(0);
    }
}