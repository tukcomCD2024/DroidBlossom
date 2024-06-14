package site.timecapsulearchive.core.domain.member_group.repository.member_group_repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleFixture;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleSkinFixture;
import site.timecapsulearchive.core.common.fixture.domain.GroupCapsuleOpenFixture;
import site.timecapsulearchive.core.common.fixture.domain.GroupFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberGroupFixture;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.GroupCapsuleOpen;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleMemberDto;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member_group.entity.MemberGroup;

@TestConstructor(autowireMode = AutowireMode.ALL)
class MemberGroupQueryRepositoryImplTest extends RepositoryTest {

    private final MemberGroupQueryRepository memberGroupQueryRepository;

    private Long capsuleId;
    private Long groupId;
    private Long memberId;

    MemberGroupQueryRepositoryImplTest(JPAQueryFactory jpaQueryFactory) {
        this.memberGroupQueryRepository = new MemberGroupQueryRepositoryImpl(jpaQueryFactory);
    }

    @BeforeEach
    void setup(@Autowired EntityManager entityManager) {
        //그룹
        Group group = GroupFixture.group();
        entityManager.persist(group);
        groupId = group.getId();

        //그룹장
        Member member = MemberFixture.member(0);
        entityManager.persist(member);
        memberId = member.getId();

        MemberGroup owner = MemberGroupFixture.groupOwner(member, group);
        entityManager.persist(owner);

        //그룹원
        List<Member> members = MemberFixture.members(1, 10);
        members.forEach(entityManager::persist);

        List<MemberGroup> memberGroups = MemberGroupFixture.memberGroups(members, group);
        memberGroups.forEach(entityManager::persist);

        //그룹 캡슐 스킨
        CapsuleSkin capsuleSkin = CapsuleSkinFixture.capsuleSkin(member);
        entityManager.persist(capsuleSkin);

        //그룹 캡슐
        Capsule capsule = CapsuleFixture.groupCapsule(member, capsuleSkin, group);
        entityManager.persist(capsule);
        capsuleId = capsule.getId();

        //그룹 캡슐 개봉 상태
        members.add(member);
        List<GroupCapsuleOpen> groupCapsuleOpens = GroupCapsuleOpenFixture.groupCapsuleOpens(group,
            false, capsule, members);
        groupCapsuleOpens.forEach(entityManager::persist);
    }

    /**
     * 테스트 케이스
     */
    @Test
    void 그룹원이_그룹_캡슐의_그룹원_목록을_조회하면_그룹_캡슐의_그룹원_목록이_반환된다() {
        //given
        //when
        List<GroupCapsuleMemberDto> groupCapsuleMembers = memberGroupQueryRepository.findGroupCapsuleMembers(
            groupId, capsuleId);

        //then
        assertThat(groupCapsuleMembers).isNotEmpty();
    }

    @Test
    void 그룹원이_그룹_캡슐의_그룹원_목록을_조회하면_그룹_캡슐의_그룹원_목록의_필드들이_반환된다() {
        //given
        //when
        List<GroupCapsuleMemberDto> groupCapsuleMembers = memberGroupQueryRepository.findGroupCapsuleMembers(
            groupId, capsuleId);

        //then
        assertSoftly(softly -> {
            softly.assertThat(groupCapsuleMembers).allMatch(member -> member.id() != null);
            softly.assertThat(groupCapsuleMembers)
                .allMatch(member -> member.isGroupOwner() != null);
            softly.assertThat(groupCapsuleMembers).allMatch(member -> member.isOpened() != null);
            softly.assertThat(groupCapsuleMembers).allMatch(member -> member.profileUrl() != null
                && !member.profileUrl().isBlank());
        });
    }
}