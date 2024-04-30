package site.timecapsulearchive.core.domain.capsule.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import jakarta.persistence.EntityManager;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleFixture;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleSkinFixture;
import site.timecapsulearchive.core.common.fixture.domain.GroupCapsuleOpenFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.entity.GroupCapsuleOpen;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleOpenQueryRepository;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.member.entity.Member;

@TestConstructor(autowireMode = AutowireMode.ALL)
class GroupCapsuleOpenQueryRepositoryTest extends RepositoryTest {

    private final JdbcTemplate jdbcTemplate;
    private final GroupCapsuleOpenQueryRepository groupCapsuleOpenRepository;
    private Capsule capsule;
    private Member groupLeader;
    private List<Member> groupMembers;
    private List<GroupCapsuleOpen> groupCapsuleOpens;

    public GroupCapsuleOpenQueryRepositoryTest(
        JdbcTemplate jdbcTemplate,
        DataSource dataSource
    ) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.groupCapsuleOpenRepository = new GroupCapsuleOpenQueryRepository(jdbcTemplate);
    }

    @BeforeEach
    @Transactional
    void setUp(@Autowired EntityManager entityManager) {
        groupMembers = MemberFixture.members(1, 5);
        groupMembers.forEach(entityManager::persist);

        groupLeader = groupMembers.get(0);

        CapsuleSkin capsuleSkin = CapsuleSkinFixture.capsuleSkin(groupLeader);
        entityManager.persist(capsuleSkin);

        capsule = CapsuleFixture.capsule(groupLeader, capsuleSkin, CapsuleType.GROUP);
        entityManager.persist(capsule);

        groupCapsuleOpens = GroupCapsuleOpenFixture.groupCapsuleOpens(false, capsule, groupMembers);
        groupCapsuleOpens.forEach(entityManager::persist);
    }

    @Test
    void 그룹캡슐이_생성되면_그룹원들은_그룹_캡슐_오픈_여부를_저장할_수_있다() {
        // given
        List<Long> groupMemberIds = groupMembers.stream().map(Member::getId).toList();
        Long capsuleId = capsule.getId();

        // when
        groupCapsuleOpenRepository.bulkSave(groupMemberIds, capsule);

        Integer actualCounts = jdbcTemplate.queryForObject(
            """
                SELECT count(*) FROM group_capsule_open WHERE group_capsule_open.capsule_id = ? and group_capsule_open.member_id in (?)
                """,
            new Object[]{capsuleId, groupMemberIds},
            Integer.class
        );

        //then
        assertThat(actualCounts).isEqualTo(0);
    }

}
