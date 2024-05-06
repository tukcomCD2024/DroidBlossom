package site.timecapsulearchive.core.domain.capsule.group_capsule.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleFixture;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleSkinFixture;
import site.timecapsulearchive.core.common.fixture.domain.GroupCapsuleOpenFixture;
import site.timecapsulearchive.core.common.fixture.domain.GroupFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberGroupFixture;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.entity.GroupCapsuleOpen;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.MyGroupCapsuleDto;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.entity.MemberGroup;
import site.timecapsulearchive.core.domain.member.entity.Member;

@TestConstructor(autowireMode = AutowireMode.ALL)
class GroupCapsuleQueryRepositoryTest extends RepositoryTest {

    private final GroupCapsuleQueryRepository groupCapsuleQueryRepository;

    private Long groupLeaderId;
    private Long groupMemberId;
    private Long capsuleId;

    GroupCapsuleQueryRepositoryTest(EntityManager entityManager) {
        this.groupCapsuleQueryRepository = new GroupCapsuleQueryRepository(
            new JPAQueryFactory(entityManager));
    }

    @BeforeEach
    @Transactional
    void setup(@Autowired EntityManager entityManager) {
        //사용자
        Member owner = MemberFixture.member(0);
        groupLeaderId = owner.getId();

        // 그룹원
        List<Member> groupMember = MemberFixture.members(1, 5);
        groupMember.add(owner);
        groupMember.forEach(entityManager::persist);
        groupMemberId = groupMember.get(0).getId();

        //캡슐 스킨
        CapsuleSkin capsuleSkin = CapsuleSkinFixture.capsuleSkin(owner);
        entityManager.persist(capsuleSkin);

        //그룹
        Group group = GroupFixture.group();
        entityManager.persist(group);

        //그룹 구성
        List<MemberGroup> memberGroups = MemberGroupFixture.memberGroups(groupMember, group);
        memberGroups.forEach(entityManager::persist);

        //그룹 캡슐
        Capsule capsule = CapsuleFixture.groupCapsule(owner, capsuleSkin, group);
        entityManager.persist(capsule);
        capsuleId = capsule.getId();

        //그룹 캡슐 오픈 여부
        List<GroupCapsuleOpen> groupCapsuleOpens = GroupCapsuleOpenFixture.groupCapsuleOpens(false,
            capsule, groupMember);
        groupCapsuleOpens.forEach(entityManager::persist);
    }

    @Test
    void 그룹장은_그룹_캡슐의_상세_내용을_조회할_수_있다() {
        // given
        //when
        GroupCapsuleDetailDto detailDto = groupCapsuleQueryRepository.findGroupCapsuleDetailDtoByCapsuleId(
            capsuleId).orElseThrow();

        //then
        assertThat(detailDto).isNotNull();
    }

    @Test
    void 그룹캡슐_아이디가_아니면_그룹_캡슐의_상세_내용을_조회할_수_없다() {
        //given
        Long notCapsuleId = -1L;

        //when
        Optional<GroupCapsuleDetailDto> detailDto = groupCapsuleQueryRepository.findGroupCapsuleDetailDtoByCapsuleId(
            notCapsuleId);

        //then
        assertThat(detailDto).isEmpty();
    }

    @Test
    void 그룹장은_그룹_캡슐의_요약_내용을_조회할_수_있다() {
        // given
        //when
        Optional<GroupCapsuleSummaryDto> detailDto = groupCapsuleQueryRepository.findGroupCapsuleSummaryDtoByCapsuleId(
            capsuleId);

        //then
        assertThat(detailDto).isPresent();
    }

    @Test
    void 그룹캡슐_아이디가_아니면_그룹_캡슐의_요약_내용을_조회할_수_없다() {
        //given
        Long notCapsuleId = -1L;

        //when
        Optional<GroupCapsuleSummaryDto> detailDto = groupCapsuleQueryRepository.findGroupCapsuleSummaryDtoByCapsuleId(
            notCapsuleId);
        //then
        assertThat(detailDto).isEmpty();
    }

    @Test
    void 사용자가_사용자가_만든_그룹_캡슐을_조회하면_사용자가_만든_그룹_캡슐만_나온다() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now().plusDays(1);

        //when
        Slice<MyGroupCapsuleDto> groupCapsules = groupCapsuleQueryRepository.findMyGroupCapsuleSlice(
            groupLeaderId, size, now);

        //then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(groupCapsules.hasContent()).isTrue();
            assertThat(groupCapsules).allMatch(
                capsule -> capsule.capsuleType().equals(CapsuleType.GROUP));
            assertThat(groupCapsules).allMatch(capsule -> capsule.createdAt().isBefore(now));
        });
    }
}