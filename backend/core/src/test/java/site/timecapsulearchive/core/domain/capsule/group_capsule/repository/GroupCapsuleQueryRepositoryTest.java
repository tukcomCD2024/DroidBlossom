package site.timecapsulearchive.core.domain.capsule.group_capsule.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
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
import site.timecapsulearchive.core.domain.capsule.data.dto.CapsuleBasicInfoDto;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.entity.GroupCapsuleOpen;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleMemberSummaryDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleSliceRequestDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member_group.entity.MemberGroup;

@TestConstructor(autowireMode = AutowireMode.ALL)
class GroupCapsuleQueryRepositoryTest extends RepositoryTest {

    private static final int MAX_COUNT = 20;
    private final GroupCapsuleQueryRepository groupCapsuleQueryRepository;

    private Long groupLeaderId;
    private Long lastCapsuleId;
    private Long groupId;
    private Capsule capsule;

    GroupCapsuleQueryRepositoryTest(JPAQueryFactory jpaQueryFactory) {
        this.groupCapsuleQueryRepository = new GroupCapsuleQueryRepository(jpaQueryFactory);
    }

    @BeforeEach
    @Transactional
    void setup(@Autowired EntityManager entityManager) {
        // 그룹원
        List<Member> groupMember = MemberFixture.members(1, 5);
        groupMember.forEach(entityManager::persist);
        Member owner = groupMember.get(0);
        groupLeaderId = owner.getId();

        //캡슐 스킨
        CapsuleSkin capsuleSkin = CapsuleSkinFixture.capsuleSkin(owner);
        entityManager.persist(capsuleSkin);

        //그룹
        Group group = GroupFixture.group();
        entityManager.persist(group);
        groupId = group.getId();

        //그룹 구성
        List<MemberGroup> memberGroups = MemberGroupFixture.memberGroups(groupMember, group);
        memberGroups.forEach(entityManager::persist);

        //그룹 캡슐
        List<Capsule> capsules = CapsuleFixture.groupCapsules(owner, capsuleSkin, group, MAX_COUNT);
        for (Capsule c : capsules) {
            entityManager.persist(c);

            List<GroupCapsuleOpen> groupCapsuleOpens = GroupCapsuleOpenFixture.groupCapsuleOpens(
                group,
                false,
                c, groupMember);
            groupCapsuleOpens.forEach(entityManager::persist);
        }
        capsule = capsules.get(0);
        lastCapsuleId = capsules.get(capsules.size() - 1).getId();
    }

    @Test
    void 그룹캡슐_아이디로_그룹_캡슐의_상세_조회_하면_상세_내용을_조회할_수_있다() {
        // given
        //when
        GroupCapsuleDetailDto groupCapsuleDetailDto = groupCapsuleQueryRepository.findGroupCapsuleDetailDtoByCapsuleId(
            capsule.getId()).orElseThrow();
        CapsuleDetailDto capsuleDetailDto = groupCapsuleDetailDto.capsuleDetailDto();

        //then
        assertSoftly(
            softly -> {
                softly.assertThat(capsuleDetailDto).isNotNull();
                softly.assertThat(capsuleDetailDto.capsuleType()).isEqualTo(capsule.getType());
                softly.assertThat(capsuleDetailDto.title()).isEqualTo(capsule.getTitle());
                softly.assertThat(capsuleDetailDto.content()).isEqualTo(capsule.getContent());
                softly.assertThat(capsuleDetailDto.capsuleId()).isEqualTo(capsule.getId());
            }
        );
    }

    @Test
    void 그룹캡슐_아이디로_그룹_캡슐의_상세_조회_하면_그룹원_정보를_조회할_수_있다() {
        //given
        //when
        GroupCapsuleDetailDto detailDto = groupCapsuleQueryRepository.findGroupCapsuleDetailDtoByCapsuleId(
            capsule.getId()).orElseThrow();
        List<GroupCapsuleMemberSummaryDto> summaryDto = detailDto.members();

        //then
        assertSoftly(
            softly -> {
                softly.assertThat(summaryDto).isNotEmpty();
                softly.assertThat(summaryDto).allMatch(dto -> !dto.isOpened());
                softly.assertThat(summaryDto).allMatch(dto -> !dto.profileUrl().isEmpty());
                softly.assertThat(summaryDto).allMatch(dto -> !dto.nickname().isEmpty());
            });
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
    void 그룹캡슐_아이디로_그룹_캡슐의_요약_조회_하면_요약_내용을_조회할_수_있다() {
        // given
        //when
        GroupCapsuleSummaryDto capsuleSummaryDto = groupCapsuleQueryRepository.findGroupCapsuleSummaryDtoByCapsuleId(
            capsule.getId()).orElseThrow();

        //then
        assertSoftly(
            softly -> {
                softly.assertThat(capsuleSummaryDto).isNotNull();
                softly.assertThat(capsuleSummaryDto.title()).isEqualTo(capsule.getTitle());
                softly.assertThat(capsuleSummaryDto.point()).isEqualTo(capsule.getPoint());
            }
        );
    }

    @Test
    void 그룹캡슐_아이디로_그룹_캡슐의_요약_조회_하면_그룹원_정보를_조회할_수_있다() {
        // given
        //when
        GroupCapsuleSummaryDto groupCapsuleSummaryDto = groupCapsuleQueryRepository.findGroupCapsuleSummaryDtoByCapsuleId(
            capsule.getId()).orElseThrow();

        //then
        assertSoftly(
            softly -> {
                softly.assertThat(groupCapsuleSummaryDto.groupMembers()).isNotEmpty();
                softly.assertThat(groupCapsuleSummaryDto.groupMembers())
                        .allMatch(dto -> dto.id() != null);
                softly.assertThat(groupCapsuleSummaryDto.groupMembers())
                    .allMatch(dto -> !dto.isOpened());
                softly.assertThat(groupCapsuleSummaryDto.groupMembers())
                    .allMatch(dto -> !dto.profileUrl().isEmpty());
                softly.assertThat(groupCapsuleSummaryDto.groupMembers())
                    .allMatch(dto -> !dto.nickname().isEmpty());
            });
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
        Slice<CapsuleBasicInfoDto> groupCapsules = groupCapsuleQueryRepository.findMyGroupCapsuleSlice(
            groupLeaderId, size, now);

        //then
        assertSoftly(softly -> {
            assertThat(groupCapsules.hasContent()).isTrue();
            assertThat(groupCapsules).allMatch(c -> c.capsuleType().equals(CapsuleType.GROUP));
            assertThat(groupCapsules).allMatch(c -> c.createdAt().isBefore(now));
        });
    }

    @Test
    void 사용자가_그룹_캡슐_목록을_조회하면_해당_그룹의_그룹캡슐이_나온다() {
        //then
        int size = 20;
        GroupCapsuleSliceRequestDto dto = GroupCapsuleSliceRequestDto.createOf(groupLeaderId,
            groupId, size, lastCapsuleId);

        //when
        Slice<CapsuleBasicInfoDto> groupCapsuleSlice = groupCapsuleQueryRepository.findGroupCapsuleSlice(
            dto);

        //then
        assertSoftly(softly -> {
            softly.assertThat(groupCapsuleSlice.hasContent()).isTrue();
            softly.assertThat(groupCapsuleSlice.getContent()).allMatch(c -> c.capsuleId() != null);
            softly.assertThat(groupCapsuleSlice.getContent())
                .allMatch(c -> c.capsuleType().equals(CapsuleType.GROUP));
            softly.assertThat(groupCapsuleSlice.getContent()).allMatch(c -> c.isOpened() != null);
            softly.assertThat(groupCapsuleSlice.getContent()).allMatch(c -> c.dueDate() != null);
            softly.assertThat(groupCapsuleSlice.getContent()).allMatch(c -> c.createdAt() != null);
            softly.assertThat(groupCapsuleSlice.getContent())
                .allMatch(c -> c.skinUrl() != null && !c.skinUrl().isBlank());
            softly.assertThat(groupCapsuleSlice.getContent())
                .allMatch(c -> c.title() != null && !c.title().isBlank());
        });
    }

    @Test
    void 사용자가_그룹_캡슐_목록의_첫_페이지_이후_다음_페이지를_조회하면_다음_페이지의_그룹캡슐이_나온다() {
        //then
        int size = 10;
        GroupCapsuleSliceRequestDto firstSliceDto = GroupCapsuleSliceRequestDto.createOf(
            groupLeaderId,
            groupId, size, null);
        Slice<CapsuleBasicInfoDto> firstGroupCapsuleSlice = groupCapsuleQueryRepository.findGroupCapsuleSlice(
            firstSliceDto);
        CapsuleBasicInfoDto capsuleBasicInfoDto = firstGroupCapsuleSlice.getContent()
            .get(0);

        //when
        GroupCapsuleSliceRequestDto dto = GroupCapsuleSliceRequestDto.createOf(groupLeaderId,
            groupId, size, capsuleBasicInfoDto.capsuleId());
        Slice<CapsuleBasicInfoDto> groupCapsuleSlice = groupCapsuleQueryRepository.findGroupCapsuleSlice(
            dto);

        //then
        assertThat(groupCapsuleSlice.hasContent()).isTrue();
    }
}