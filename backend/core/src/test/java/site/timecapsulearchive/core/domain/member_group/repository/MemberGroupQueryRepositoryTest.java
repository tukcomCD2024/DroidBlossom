package site.timecapsulearchive.core.domain.member_group.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
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
import site.timecapsulearchive.core.domain.group.repository.GroupQueryRepository;
import site.timecapsulearchive.core.domain.group.repository.GroupQueryRepositoryImpl;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member_group.entity.MemberGroup;

@TestConstructor(autowireMode = AutowireMode.ALL)
class MemberGroupQueryRepositoryTest extends RepositoryTest {

    private final static int GROUP_COUNT = 20;

    private final GroupQueryRepository groupQueryRepository;

    private Long memberId;
    private Long memberIdWithNoGroup;

    MemberGroupQueryRepositoryTest(JPAQueryFactory jpaQueryFactory) {
        this.groupQueryRepository = new GroupQueryRepositoryImpl(jpaQueryFactory);
    }

    @Transactional
    @BeforeEach
    void setup(@Autowired EntityManager entityManager) {
        //사용자
        Member member = MemberFixture.member(0);
        entityManager.persist(member);
        memberId = member.getId();

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

        //그룹에 사용자를 그룹장으로 설정
        for (int count = 0; count < GROUP_COUNT; count++) {
            MemberGroup memberGroup = MemberGroupFixture.memberGroup(member, groups.get(count),
                Boolean.TRUE);
            entityManager.persist(memberGroup);
        }

        //그룹원들
        List<Member> members = MemberFixture.members(4, 2);
        members.forEach(entityManager::persist);

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
        Slice<GroupSummaryDto> groupsSlice = groupQueryRepository.findGroupsSlice(memberId,
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
        List<GroupSummaryDto> groupsSlice = groupQueryRepository.findGroupsSlice(memberId,
            size, now).getContent();

        //then
        assertSoftly(softly -> {
            softly.assertThat(groupsSlice).allMatch(dto -> dto.id() != null);
            softly.assertThat(groupsSlice).allMatch(dto -> !dto.groupName().isBlank());
            softly.assertThat(groupsSlice).allMatch(dto -> !dto.groupDescription().isBlank());
            softly.assertThat(groupsSlice).allMatch(dto -> !dto.groupProfileUrl().isBlank());
            softly.assertThat(groupsSlice).allMatch(dto -> dto.isOwner() != null);
        });
    }

    @Test
    void 그룹이_없는_사용자로_그룹_목록을_조회하면_빈_그룹_목록이_반환된다() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now().plusDays(3);

        //when
        List<GroupSummaryDto> groupsSlice = groupQueryRepository.findGroupsSlice(
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
        List<GroupSummaryDto> groupsSlice = groupQueryRepository.findGroupsSlice(memberId,
            size, now).getContent();

        //then
        assertThat(groupsSlice.isEmpty()).isTrue();
    }
}