package site.timecapsulearchive.core.domain.group.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
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
import site.timecapsulearchive.core.common.fixture.domain.GroupFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberGroupFixture;
import site.timecapsulearchive.core.domain.group.data.dto.GroupDetailDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupSummaryDto;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member_group.entity.MemberGroup;

@TestConstructor(autowireMode = AutowireMode.ALL)
class GroupQueryRepositoryTest extends RepositoryTest {

    private final static int GROUP_COUNT = 20;

    private final GroupQueryRepository groupQueryRepository;

    private Long memberIdWithNoGroup;
    private Long groupId;
    private Long ownerId;
    private Long groupMemberId;

    GroupQueryRepositoryTest(JPAQueryFactory jpaQueryFactory) {
        this.groupQueryRepository = new GroupQueryRepositoryImpl(jpaQueryFactory);
    }

    @Transactional
    @BeforeEach
    void setup(@Autowired EntityManager entityManager) {
        //사용자
        Member member = MemberFixture.member(0);
        entityManager.persist(member);
        ownerId = member.getId();

        //그룹이 없는 사용자
        Member memberNotInGroup = MemberFixture.member(1);
        entityManager.persist(memberNotInGroup);
        memberIdWithNoGroup = memberNotInGroup.getId();

        //그룹
        List<Group> groups = new ArrayList<>();
        for (int count = 0; count < GROUP_COUNT; count++) {
            Group group = GroupFixture.group();
            entityManager.persist(group);
            groups.add(group);
        }
        groupId = groups.get(0).getId();

        //그룹에 사용자를 그룹장으로 설정
        for (int count = 0; count < GROUP_COUNT; count++) {
            MemberGroup memberGroup = MemberGroupFixture.memberGroup(member, groups.get(count),
                Boolean.TRUE);
            entityManager.persist(memberGroup);
        }

        //그룹원들
        List<Member> members = MemberFixture.members(4, 2);
        members.forEach(entityManager::persist);
        groupMemberId = members.get(0).getId();

        //그룹원들 설정
        List<MemberGroup> memberGroups = MemberGroupFixture.memberGroups(members, groups.get(0));
        memberGroups.forEach(entityManager::persist);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 15, 20})
    void 사용자와_개수_마지막_데이터_생성_시간으로_그룹_목록을_조회하면_개수만큼_그룹이_반환된다(int size) {
        //given
        ZonedDateTime now = ZonedDateTime.now().plusDays(3);

        //when
        Slice<GroupSummaryDto> groupsSlice = groupQueryRepository.findGroupSummaries(ownerId,
            size, now);

        //then
        assertThat(groupsSlice.getNumberOfElements()).isEqualTo(size);
    }

    @Test
    void 사용자와_개수_마지막_데이터_생성_시간으로_그룹_목록을_조회하면_개수만큼_그룹의_내용들이_반환된다() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now().plusDays(3);

        //when
        List<GroupSummaryDto> groupsSlice = groupQueryRepository.findGroupSummaries(ownerId,
            size, now).getContent();

        //then
        assertSoftly(softly -> {
            softly.assertThat(groupsSlice).allMatch(dto -> dto.id() != null);
            softly.assertThat(groupsSlice)
                .allMatch(dto -> !dto.groupName().isBlank());
            softly.assertThat(groupsSlice)
                .allMatch(dto -> !dto.groupProfileUrl().isBlank());
            softly.assertThat(groupsSlice).allMatch(dto -> dto.isOwner() != null);
        });
    }

    @Test
    void 그룹이_없는_사용자로_그룹_목록을_조회하면_빈_그룹_목록이_반환된다() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now().plusDays(3);

        //when
        List<GroupSummaryDto> groupsSlice = groupQueryRepository.findGroupSummaries(
                memberIdWithNoGroup,
                size,
                now)
            .getContent();

        //then
        assertThat(groupsSlice.isEmpty()).isTrue();
    }

    @Test
    void 사용자와_범위에_없는_마지막_데이터_생성_시간으로_그룹_목록을_조회하면_빈_그룹_목록이_반환된다() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now().minusDays(5);

        //when
        List<GroupSummaryDto> groupsSlice = groupQueryRepository.findGroupSummaries(ownerId,
            size, now).getContent();

        //then
        assertThat(groupsSlice.isEmpty()).isTrue();
    }

    @Test
    void 그룹_아이디와_멤버_아이디로_그룹을_조회하면_그룹_상세가_반환된다() {
        //given
        //when
        GroupDetailDto groupDetail = groupQueryRepository.findGroupDetailByGroupIdAndMemberIdExcludeMemberId(
            groupId, ownerId).orElseThrow();

        //then
        assertThat(groupDetail).isNotNull();
    }

    @Test
    void 그룹_아이디와_멤버_아이디로_그룹을_조회하면_그룹_정보를_볼_수_있다() {
        //given
        //when
        GroupDetailDto groupDetail = groupQueryRepository.findGroupDetailByGroupIdAndMemberIdExcludeMemberId(
            groupId, ownerId).orElseThrow();

        //then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(groupDetail.groupName()).isNotBlank();
            assertThat(groupDetail.groupDescription()).isNotBlank();
            assertThat(groupDetail.groupProfileUrl()).isNotBlank();
            assertThat(groupDetail.createdAt()).isNotNull();
            assertThat(groupDetail.members()).isNotEmpty();
        });
    }

    @Test
    void 그룹_아이디와_멤버_아이디_그룹을_조회하면_그룹원들의_정보를_볼_수_있다() {
        //given
        //when
        GroupDetailDto groupDetail = groupQueryRepository.findGroupDetailByGroupIdAndMemberIdExcludeMemberId(
            groupId, groupMemberId).orElseThrow();

        //then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(groupDetail.members()).isNotEmpty();
            assertThat(groupDetail.members()).allSatisfy(m -> assertThat(m.memberId()).isNotNull());
            assertThat(groupDetail.members()).allSatisfy(m -> assertThat(m.tag()).isNotBlank());
            assertThat(groupDetail.members()).allSatisfy(
                m -> assertThat(m.nickname()).isNotBlank());
            assertThat(groupDetail.members()).allSatisfy(
                m -> assertThat(m.profileUrl()).isNotBlank());
            assertThat(groupDetail.members()).allSatisfy(m -> assertThat(m.isOwner()).isNotNull());
        });
    }

    @Test
    void 그룹_아이디와_멤버_아이디로_그룹을_조회하면_본인은_포함되어_조회되지_않는다() {
        //given
        //when
        GroupDetailDto groupDetail = groupQueryRepository.findGroupDetailByGroupIdAndMemberIdExcludeMemberId(
            groupId, groupMemberId).orElseThrow();

        List<GroupMemberDto> groupMemberDtos = groupDetail.members();

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(groupMemberDtos)
                .noneMatch(member -> member.memberId().equals(groupMemberId));
        });
    }

    @Test
    void 그룹_아이디로_그룹을_상세정보를_조회하면_사용자를_제외한_그룹원의_정보를_볼_수_있다() {
        //given
        //when
        GroupDetailDto groupDetailDto = groupQueryRepository.findGroupDetailByGroupIdAndMemberIdExcludeMemberId(
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
        Optional<GroupDetailDto> groupDetailDto = groupQueryRepository.findGroupDetailByGroupIdAndMemberIdExcludeMemberId(
            notExistGroupId, ownerId);

        //then
        assertThat(groupDetailDto).isEmpty();
    }

    @Test
    void 그룹원이_그룹을_상세정보를_조회하면_그룹의_수정권한이_없다() {
        //given
        //when
        GroupDetailDto groupDetailDto = groupQueryRepository.findGroupDetailByGroupIdAndMemberIdExcludeMemberId(
                groupId, groupMemberId)
            .orElseThrow();

        //then
        assertThat(groupDetailDto.isOwner()).isFalse();
    }
}