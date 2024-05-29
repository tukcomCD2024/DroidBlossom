package site.timecapsulearchive.core.domain.group.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.common.fixture.domain.GroupFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberGroupFixture;
import site.timecapsulearchive.core.domain.group.data.dto.GroupDetailDto;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member_group.entity.MemberGroup;

@TestConstructor(autowireMode = AutowireMode.ALL)
class GroupQueryRepositoryImplTest extends RepositoryTest {

    private final GroupQueryRepository groupQueryRepository;

    private Long groupId;
    private Long ownerId;
    private Long groupMemberId;

    GroupQueryRepositoryImplTest(JPAQueryFactory jpaQueryFactory) {
        this.groupQueryRepository = new GroupQueryRepositoryImpl(jpaQueryFactory);
    }

    @Transactional
    @BeforeEach
    void setup(@Autowired EntityManager entityManager) {
        Group group = GroupFixture.group();
        entityManager.persist(group);
        groupId = group.getId();

        Member member = MemberFixture.member(1);
        entityManager.persist(member);
        ownerId = member.getId();

        MemberGroup memberGroup = MemberGroupFixture.groupOwner(member, group);
        entityManager.persist(memberGroup);

        List<Member> members = MemberFixture.members(2, 10);
        for (Member m : members) {
            entityManager.persist(m);

            MemberGroup mg = MemberGroupFixture.memberGroup(m, group, Boolean.FALSE);
            entityManager.persist(mg);
        }
        groupMemberId = members.get(0).getId();
    }

    @Test
    void 그룹_아이디로_그룹을_상세정보를_조회하면_그룹의_상세정보를_볼_수_있다() {
        //given
        //when
        GroupDetailDto groupDetailDto = groupQueryRepository.findGroupDetailByGroupIdAndMemberId(
                groupId, ownerId)
            .orElseThrow();

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(groupDetailDto.groupName()).isNotBlank();
            softly.assertThat(groupDetailDto.groupDescription()).isNotBlank();
            softly.assertThat(groupDetailDto.groupProfileUrl()).isNotBlank();
            softly.assertThat(groupDetailDto.groupProfileUrl()).isNotBlank();
            softly.assertThat(groupDetailDto.createdAt()).isNotNull();
            softly.assertThat(groupDetailDto.members()).isNotEmpty();
        });
    }

    @Test
    void 그룹_아이디로_그룹을_상세정보를_조회하면_사용자를_제외한_그룹원의_정보를_볼_수_있다() {
        //given
        //when
        GroupDetailDto groupDetailDto = groupQueryRepository.findGroupDetailByGroupIdAndMemberId(
                groupId, ownerId)
            .orElseThrow();

        //then
        assertThat(groupDetailDto.members()).noneMatch(m -> m.memberId().equals(ownerId));
    }

    @Test
    void 존재하지_않는_그룹_아이디로_그룹을_상세정보를_조회하면_예외가_발생한다() {
        //given
        Long notExistGroupId = 999L;

        //when
        Optional<GroupDetailDto> groupDetailDto = groupQueryRepository.findGroupDetailByGroupIdAndMemberId(
            notExistGroupId, ownerId);

        //then
        assertThat(groupDetailDto).isEmpty();
    }

    @Test
    void 그룹장이_그룹을_상세정보를_조회하면_그룹의_수정권한을_가진다() {
        //given
        //when
        GroupDetailDto groupDetailDto = groupQueryRepository.findGroupDetailByGroupIdAndMemberId(
                groupId, ownerId)
            .orElseThrow();

        //then
        assertThat(groupDetailDto.isOwner()).isTrue();
    }

    @Test
    void 그룹원이_그룹을_상세정보를_조회하면_그룹의_수정권한이_없다() {
        //given
        //when
        GroupDetailDto groupDetailDto = groupQueryRepository.findGroupDetailByGroupIdAndMemberId(
                groupId, groupMemberId)
            .orElseThrow();

        //then
        assertThat(groupDetailDto.isOwner()).isFalse();
    }
}