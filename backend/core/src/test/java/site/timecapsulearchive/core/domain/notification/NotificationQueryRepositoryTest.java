package site.timecapsulearchive.core.domain.notification;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.ZonedDateTime;
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
import site.timecapsulearchive.core.common.fixture.domain.NotificationCategoryFixture;
import site.timecapsulearchive.core.common.fixture.domain.NotificationFixture;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.notification.data.dto.MemberNotificationDto;
import site.timecapsulearchive.core.domain.notification.entity.CategoryName;
import site.timecapsulearchive.core.domain.notification.entity.Notification;
import site.timecapsulearchive.core.domain.notification.entity.NotificationCategory;
import site.timecapsulearchive.core.domain.notification.repository.NotificationQueryRepository;
import site.timecapsulearchive.core.domain.notification.repository.NotificationQueryRepositoryImpl;

@TestConstructor(autowireMode = AutowireMode.ALL)
class NotificationQueryRepositoryTest extends RepositoryTest {

    private final NotificationQueryRepository notificationQueryRepository;

    private Member member;
    private Member zeroNotificationMember;

    NotificationQueryRepositoryTest(JPAQueryFactory jpaQueryFactory) {
        this.notificationQueryRepository = new NotificationQueryRepositoryImpl(jpaQueryFactory);
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
    }

    @Test
    void 특정_사용자로_알림_목록을_조회하면_알림_리스트가_반환된다() {
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now().plusMinutes(1);

        Slice<MemberNotificationDto> slice = notificationQueryRepository.findNotificationSliceByMemberId(
            member.getId(), size, now);

        assertThat(slice.getContent()).allMatch(
            notification -> notification.createdAt().isBefore(now));
    }

    @ParameterizedTest
    @ValueSource(ints = {20, 15, 10, 5, 1})
    void 원하는_크기로_알림_목록을_조회하면_크기에_맞는_알림_리스트가_반환된다(int size) {
        ZonedDateTime now = ZonedDateTime.now().plusMinutes(1);

        Slice<MemberNotificationDto> slice = notificationQueryRepository.findNotificationSliceByMemberId(
            member.getId(), size, now);

        assertThat(slice.getContent().size()).isEqualTo(size);
    }

    @Test
    void 유효하지_않은_시간으로_알림_목록을_조회하면_빈_알림_리스트가_반환된다() {
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now().minusDays(5);

        Slice<MemberNotificationDto> slice = notificationQueryRepository.findNotificationSliceByMemberId(
            member.getId(), size, now);

        assertThat(slice.getContent().size()).isZero();
    }

    @Test
    void 알림이_존재하지_않는_사용자로_알림_목록을_조회하면_빈_알림_리스트가_반환된다() {
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now().plusMinutes(1);

        Slice<MemberNotificationDto> slice = notificationQueryRepository.findNotificationSliceByMemberId(
            zeroNotificationMember.getId(), size, now);

        assertThat(slice.getContent().size()).isZero();
    }
}
