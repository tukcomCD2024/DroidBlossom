package site.timecapsulearchive.core.domain.capsule.group_capsule.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.ZonedDateTime;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.common.fixture.GroupFixture;
import site.timecapsulearchive.core.common.fixture.MemberGroupFixture;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleFixture;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleSkinFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.MyGroupCapsuleDto;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.entity.MemberGroup;
import site.timecapsulearchive.core.domain.member.entity.Member;

@TestConstructor(autowireMode = AutowireMode.ALL)
class GroupCapsuleQueryRepositoryTest extends RepositoryTest {

    private final GroupCapsuleQueryRepository groupCapsuleQueryRepository;

    private Long memberId;

    GroupCapsuleQueryRepositoryTest(EntityManager entityManager) {
        this.groupCapsuleQueryRepository = new GroupCapsuleQueryRepository(
            new JPAQueryFactory(entityManager));
    }

    @BeforeEach
    @Transactional
    void setup(@Autowired EntityManager entityManager) {
        //사용자
        Member member = MemberFixture.member(0);
        entityManager.persist(member);
        memberId = member.getId();

        //캡슐 스킨
        CapsuleSkin capsuleSkin = CapsuleSkinFixture.capsuleSkin(member);
        entityManager.persist(capsuleSkin);

        //그룹
        Group group = GroupFixture.group();
        entityManager.persist(group);

        //그룹원
        MemberGroup memberGroup = MemberGroupFixture.memberGroup(member, group);
        entityManager.persist(memberGroup);

        //그룹 캡슐
        CapsuleFixture.groupCapsules(20, member, capsuleSkin, group)
            .forEach(entityManager::persist);
    }

    @Test
    void 사용자가_사용자가_만든_그룹_캡슐을_조회하면_사용자가_만든_그룹_캡슐만_나온다() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now().plusDays(1);

        //when
        Slice<MyGroupCapsuleDto> groupCapsules = groupCapsuleQueryRepository.findMyGroupCapsuleSlice(
            memberId, size, now);

        //then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(groupCapsules.hasContent()).isTrue();
            assertThat(groupCapsules).allMatch(
                capsule -> capsule.capsuleType().equals(CapsuleType.GROUP));
            assertThat(groupCapsules).allMatch(capsule -> capsule.createdAt().isBefore(now));
        });
    }
}