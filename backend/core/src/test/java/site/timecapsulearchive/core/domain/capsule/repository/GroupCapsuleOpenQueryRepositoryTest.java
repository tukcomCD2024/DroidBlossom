package site.timecapsulearchive.core.domain.capsule.repository;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final GroupCapsuleOpenQueryRepository groupCapsuleOpenRepository;
    private Capsule capsule;
    private List<Member> groupMembers;
    private List<GroupCapsuleOpen> groupCapsuleOpens;

    public GroupCapsuleOpenQueryRepositoryTest(
        JdbcTemplate jdbcTemplate,
        DataSource dataSource
    ) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.groupCapsuleOpenRepository = new GroupCapsuleOpenQueryRepository(jdbcTemplate);
    }

    @Transactional
    @BeforeEach
    void setUp(@Autowired EntityManager entityManager) {
        groupMembers = MemberFixture.members(1, 5);
        groupMembers.forEach(entityManager::persist);

        Member groupLeader = groupMembers.get(0);

        CapsuleSkin capsuleSkin = CapsuleSkinFixture.capsuleSkin(groupLeader);
        entityManager.persist(capsuleSkin);

        capsule = CapsuleFixture.capsule(groupLeader, capsuleSkin, CapsuleType.GROUP);
        entityManager.persist(capsule);

        groupCapsuleOpens = GroupCapsuleOpenFixture.groupCapsuleOpens(false, capsule, groupMembers);
    }

    @Test
    void 그룹캡슐이_생성되면_그룹원들은_그룹_캡슐_오픈_여부를_저장할_수_있다() {
        // given
        List<Long> groupMemberIds = groupMembers.stream().map(Member::getId).toList();
        Long capsuleId = capsule.getId();

        // when
        groupCapsuleOpenRepository.bulkSave(groupMemberIds, capsule);

        //then
        String sql = "SELECT count(*) from group_capsule_open WHERE capsule_id = (:capsuleId) and member_id in (:groupMemberIds)";
        MapSqlParameterSource parameters = new MapSqlParameterSource("groupMemberIds",
            groupMemberIds);
        parameters.addValue("capsuleId", capsuleId);

        Integer actualGroupMemberCount = namedParameterJdbcTemplate.queryForObject(
            sql,
            parameters,
            Integer.class
        );

        assertThat(actualGroupMemberCount).isEqualTo(groupCapsuleOpens.size());
    }
}
