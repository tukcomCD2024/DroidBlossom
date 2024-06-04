package site.timecapsulearchive.core.domain.member_group.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
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
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupSendingInviteMemberDto;
import site.timecapsulearchive.core.domain.member_group.entity.GroupInvite;
import site.timecapsulearchive.core.domain.member_group.repository.group_invite_repository.GroupInviteQueryRepository;
import site.timecapsulearchive.core.domain.member_group.repository.group_invite_repository.GroupInviteQueryRepositoryImpl;

@TestConstructor(autowireMode = AutowireMode.ALL)
class GroupInviteQueryRepositoryTest extends RepositoryTest {

    private static final int MAX_GROUP_COUNT = 2;

    private final GroupInviteQueryRepository groupInviteRepository;

    private Long groupId;
    private Long groupOwnerId;
    private Long groupMemberId;

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
        groupOwnerId = groupOwners.get(0).getId();

        //그룹 초대 올 그룹원
        Member groupMember = MemberFixture.member(2);
        entityManager.persist(groupMember);
        groupMemberId = groupMember.getId();

        // 그룹들
        List<Group> groups = GroupFixture.groups(0, MAX_GROUP_COUNT);
        groups.forEach(entityManager::persist);
        groupId = groups.get(0).getId();

        // 그룹원에게 초대온 그룹 초대들
        for (int i = 0; i < MAX_GROUP_COUNT; i++) {
            GroupInvite groupInvite = GroupInvite.createOf(groups.get(i), groupOwners.get(i),
                groupMember);
            entityManager.persist(groupInvite);
        }
    }

    @Test
    void 사용자는_자신에게_온_그룹_초대_목록을_조회하면_그룹_초대목록이_나온다() {
        //given
        Long memberId = groupMemberId;
        int size = 1;
        ZonedDateTime createAt = ZonedDateTime.now().plusDays(1);

        //when
        Slice<GroupInviteSummaryDto> groupInvitesSummary = groupInviteRepository.findGroupReceivingInvitesSlice(
            memberId, size, createAt);

        //then
        assertThat(groupInvitesSummary.getContent()).isNotEmpty();
    }

    @Test
    void 사용자는_자신에게_온_그룹_초대_목록에서_그룹_정보와_그룹장을_알_수_있다() {
        //given
        Long memberId = groupMemberId;
        int size = 1;
        ZonedDateTime createAt = ZonedDateTime.now().plusDays(1);

        //when
        Slice<GroupInviteSummaryDto> groupInvitesSummary = groupInviteRepository.findGroupReceivingInvitesSlice(
            memberId, size, createAt);

        //then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(groupInvitesSummary).allMatch(g -> !g.groupName().isEmpty());
            assertThat(groupInvitesSummary)
                .allMatch(g -> !g.groupProfileUrl().isEmpty());
            assertThat(groupInvitesSummary).allMatch(g -> !g.description().isEmpty());
            assertThat(groupInvitesSummary).allMatch(g -> !g.groupOwnerName().isEmpty());
        });
    }

    @Test
    void 사용자는_그룹_초대_받은_목록_첫_페이지를_조회_후_다음_페이지에서_그룹_초대_받은_목록을_조회_할_수_있다() {
        //given
        Long memberId = groupMemberId;
        int size = 1;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(3);
        Slice<GroupInviteSummaryDto> firstSlice = groupInviteRepository.findGroupReceivingInvitesSlice(
            memberId, size, now);

        //when
        GroupInviteSummaryDto dto = firstSlice.getContent().get(0);
        Slice<GroupInviteSummaryDto> groupInvitesSummary = groupInviteRepository.findGroupReceivingInvitesSlice(
            memberId, size, dto.groupReceivingInviteTime().plusSeconds(1L));

        //then
        assertThat(groupInvitesSummary.getContent()).isNotEmpty();
    }

    @Test
    void 그룹장은_자신이_보낸_그룹_초대_목록을_조회하면_그룹_초대목록이_나온다() {
        //given
        //when
        List<GroupSendingInviteMemberDto> groupSendingInvites = groupInviteRepository.findGroupSendingInvites(
            groupOwnerId, groupId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(groupSendingInvites).isNotEmpty();
            softly.assertThat(groupSendingInvites).allMatch(dto -> dto.id() != null);
            softly.assertThat(groupSendingInvites)
                .allMatch(dto -> dto.nickname() != null && !dto.nickname().isBlank());
            softly.assertThat(groupSendingInvites)
                .allMatch(dto -> dto.profileUrl() != null && !dto.profileUrl().isBlank());
            softly.assertThat(groupSendingInvites)
                .allMatch(dto -> dto.sendingInvitesCreatedAt() != null);
        });
    }

    @Test
    void 그룹원은_자신이_보낸_그룹_초대_목록을_조회하면_빈_리스트가_나온다() {
        //given
        //when
        List<GroupSendingInviteMemberDto> groupSendingInvites = groupInviteRepository.findGroupSendingInvites(
            groupMemberId, groupId);

        //then
        assertThat(groupSendingInvites).isEmpty();
    }
}
