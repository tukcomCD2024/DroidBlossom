package site.timecapsulearchive.core.domain.capsule.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleFixture;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleSkinFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.member.entity.Member;

@TestConstructor(autowireMode = AutowireMode.ALL)
class GroupCapsuleQueryRepositoryTest extends RepositoryTest {

    private final GroupCapsuleQueryRepository groupCapsuleQueryRepository;

    private static Capsule capsule;
    private static Member groupLeader;
    private static CapsuleSkin capsuleSkin;


    public GroupCapsuleQueryRepositoryTest(EntityManager entityManager) {
        this.groupCapsuleQueryRepository = new GroupCapsuleQueryRepository(
            new JPAQueryFactory(entityManager));
    }

    @BeforeEach
    @Transactional
    void setUp(@Autowired EntityManager entityManager) {
        groupLeader = MemberFixture.member(1);
        entityManager.persist(groupLeader);

        capsuleSkin = CapsuleSkinFixture.capsuleSkin(groupLeader);
        entityManager.persist(capsuleSkin);

        capsule = CapsuleFixture.capsule(groupLeader, capsuleSkin, CapsuleType.GROUP);
        entityManager.persist(capsule);
    }


    @Test
    void 그룹장은_그룹_캡슐의_상세_내용을_조회할_수_있다() {
        // given
        Long capsuleId = capsule.getId();

        //when
        Optional<GroupCapsuleDetailDto> detailDto = groupCapsuleQueryRepository.findGroupCapsuleDetailDtoByCapsuleId(
            capsuleId);

        //then
        assertThat(detailDto.isPresent()).isTrue();
    }

    @Test
    void 그룹캡슐_아이디가_아니면_그룹_캡슐의_상세_내용을_조회할_수_없다() {
        //given
        Long notCapsuleId = 999L;

        //when
        Optional<GroupCapsuleDetailDto> detailDto = groupCapsuleQueryRepository.findGroupCapsuleDetailDtoByCapsuleId(
            notCapsuleId);

        //then
        assertThat(detailDto).isEmpty();
    }

    @Test
    void 그룹장은_그룹_캡슐의_요약_내용을_조회할_수_있다() {
        // given
        Long capsuleId = capsule.getId();

        //when
        Optional<GroupCapsuleSummaryDto> detailDto = groupCapsuleQueryRepository.findGroupCapsuleSummaryDtoByCapsuleId(
            capsuleId);

        //then
        assertThat(detailDto.isPresent()).isTrue();
    }

    @Test
    void 그룹캡슐_아이디가_아니면_그룹_캡슐의_요약_내용을_조회할_수_없다() {
        //given
        Long notCapsuleId = 999L;

        //when
        Optional<GroupCapsuleSummaryDto> detailDto = groupCapsuleQueryRepository.findGroupCapsuleSummaryDtoByCapsuleId(
            notCapsuleId);
        //then
        assertThat(detailDto).isEmpty();
    }

}
