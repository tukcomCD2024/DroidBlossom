package site.timecapsulearchive.core.domain.member_group.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.common.fixture.domain.GroupFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupInviteSummaryDto;
import site.timecapsulearchive.core.domain.member_group.entity.GroupInvite;
import site.timecapsulearchive.core.domain.member_group.repository.groupInviteRepository.GroupInviteQueryRepository;
import site.timecapsulearchive.core.domain.member_group.repository.groupInviteRepository.GroupInviteQueryRepositoryImpl;

@TestConstructor(autowireMode = AutowireMode.ALL)
class GroupInviteQueryRepositoryTest extends RepositoryTest {

    private static final int MAX_GROUP_COUNT = 2;
    private final GroupInviteQueryRepository groupInviteRepository;
    private Member groupMember;

    GroupInviteQueryRepositoryTest(
        JdbcTemplate jdbcTemplate,
        JPAQueryFactory jpaQueryFactory
    ) {
        this.groupInviteRepository = new GroupInviteQueryRepositoryImpl(
            jdbcTemplate, jpaQueryFactory);
    }

    @Transactional
    @BeforeEach
    void setUp(@Autowired EntityManager entityManager) {
        // 그룹 초대 할 그룹장들
        List<Member> groupOwners = MemberFixture.members(0, MAX_GROUP_COUNT);
        groupOwners.forEach(entityManager::persist);

        //그룹 초대 올 그룹원
        groupMember = MemberFixture.member(2);
        entityManager.persist(groupMember);

        // 그룹들
        List<Group> groups = GroupFixture.groups(0, MAX_GROUP_COUNT);
        groups.forEach(entityManager::persist);

        // 그룹원에게 초대온 그룹 초대들
        for (int i = 0; i < MAX_GROUP_COUNT; i++) {
            GroupInvite groupInvite = GroupInvite.createOf(groups.get(i), groupOwners.get(i),
                groupMember);
            entityManager.persist(groupInvite);
        }
    }

    @Test
    void 사용자는_자신에게_온_그룹_초대_목록을_조회할_수_있다() {
        //given
        Long memberId = groupMember.getId();
        int size = 1;
        ZonedDateTime createAt = ZonedDateTime.now().plusDays(1);

        //when
        Slice<GroupInviteSummaryDto> groupInvitesSummary = groupInviteRepository.findGroupRecetpionInvitesSlice(
            memberId, size, createAt);

        //then
        assertThat(groupInvitesSummary.getContent()).isNotNull();
    }

    @Test
    void 사용자는_자신에게_온_그룹_초대_목록에서_그룹_정보와_그룹장을_알_수_있다() {
        //given
        Long memberId = groupMember.getId();
        int size = 1;
        ZonedDateTime createAt = ZonedDateTime.now().plusDays(1);

        //when
        Slice<GroupInviteSummaryDto> groupInvitesSummary = groupInviteRepository.findGroupRecetpionInvitesSlice(
            memberId, size, createAt);

        //then
        SoftAssertions.assertSoftly(softly -> {
            Assertions.assertThat(groupInvitesSummary).allMatch(g -> !g.groupName().isEmpty());
            Assertions.assertThat(groupInvitesSummary)
                .allMatch(g -> !g.groupProfileUrl().isEmpty());
            Assertions.assertThat(groupInvitesSummary).allMatch(g -> !g.description().isEmpty());
            Assertions.assertThat(groupInvitesSummary).allMatch(g -> !g.groupOwnerName().isEmpty());
        });
    }

    @Test
    void 사용자는_조회한_그룹_초대_목록_후_그룹_초대_존재_여부를_확인_할_수_있다() {
        //given
        Long memberId = groupMember.getId();
        int size = 1;
        ZonedDateTime createAt = ZonedDateTime.now().plusDays(1);

        //when
        Slice<GroupInviteSummaryDto> groupInvitesSummary = groupInviteRepository.findGroupRecetpionInvitesSlice(
            memberId, size, createAt);

        //then
        assertThat(groupInvitesSummary.hasNext()).isTrue();
    }


}
