package site.timecapsulearchive.core.domain.capsule.group_capsule.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleFixture;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleSkinFixture;
import site.timecapsulearchive.core.common.fixture.domain.GroupCapsuleOpenFixture;
import site.timecapsulearchive.core.common.fixture.domain.GroupFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.dto.CapsuleDtoFixture;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.GroupCapsuleOpen;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.exception.GroupCapsuleOpenNotFoundException;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.CapsuleRepository;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleOpenRepository;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberSummaryDto;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.global.error.ErrorCode;

class GroupCapsuleServiceTest {

    private final Long capsuleId = 1L;
    private final int groupMemberCount = 3;

    private final CapsuleRepository capsuleRepository = mock(CapsuleRepository.class);
    private final GroupCapsuleQueryRepository groupCapsuleQueryRepository = mock(
        GroupCapsuleQueryRepository.class);
    private final GroupCapsuleOpenRepository groupCapsuleOpenRepository = mock(
        GroupCapsuleOpenRepository.class);

    private final GroupCapsuleService groupCapsuleService = new GroupCapsuleService(
        capsuleRepository, groupCapsuleQueryRepository, groupCapsuleOpenRepository);

    @Test
    void 개봉된_그룹_캡슐의_상세_내용을_볼_수_있다() {
        //given
        given(
            groupCapsuleQueryRepository.findGroupCapsuleDetailDtoByCapsuleId(anyLong())).willReturn(
            CapsuleDtoFixture.getGroupCapsuleDetailDto(capsuleId, true, ZonedDateTime.now(), 3)
        );

        //when
        GroupCapsuleDetailDto response = groupCapsuleService.findGroupCapsuleDetailByGroupIdAndCapsuleId(
            capsuleId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            CapsuleDetailDto detailDto = response.capsuleDetailDto();
            List<GroupMemberSummaryDto> members = response.members();
            softly.assertThat(response).isNotNull();
            softly.assertThat(detailDto.isOpened()).isTrue();
            softly.assertThat(detailDto.title()).isNotBlank();
            softly.assertThat(detailDto.content()).isNotBlank();
            softly.assertThat(detailDto.images()).isNotBlank();
            softly.assertThat(detailDto.videos()).isNotBlank();
            softly.assertThat(members.size()).isEqualTo(groupMemberCount);
        });
    }

    @Test
    void 개봉일이_없고_개봉된_캡슐의_상세_내용을_볼_수_있다() {
        //given
        given(
            groupCapsuleQueryRepository.findGroupCapsuleDetailDtoByCapsuleId(anyLong())).willReturn(
            CapsuleDtoFixture.getGroupCapsuleDetailDto(capsuleId, true, null, 3)
        );

        //when
        GroupCapsuleDetailDto response = groupCapsuleService.findGroupCapsuleDetailByGroupIdAndCapsuleId(
            capsuleId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            CapsuleDetailDto detailDto = response.capsuleDetailDto();
            List<GroupMemberSummaryDto> members = response.members();
            softly.assertThat(response).isNotNull();
            softly.assertThat(detailDto.isOpened()).isTrue();
            softly.assertThat(detailDto.title()).isNotBlank();
            softly.assertThat(detailDto.content()).isNotBlank();
            softly.assertThat(detailDto.images()).isNotBlank();
            softly.assertThat(detailDto.videos()).isNotBlank();
            softly.assertThat(members.size()).isEqualTo(groupMemberCount);
        });
    }

    @Test
    void 개봉일이_없고_개봉되지_않은_캡슐의_상세_내용을_볼_수_있다() {
        //given
        given(
            groupCapsuleQueryRepository.findGroupCapsuleDetailDtoByCapsuleId(anyLong())).willReturn(
            CapsuleDtoFixture.getGroupCapsuleDetailDto(capsuleId, false, null, 3)
        );

        //when
        GroupCapsuleDetailDto response = groupCapsuleService.findGroupCapsuleDetailByGroupIdAndCapsuleId(
            capsuleId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            CapsuleDetailDto detailDto = response.capsuleDetailDto();
            List<GroupMemberSummaryDto> members = response.members();
            softly.assertThat(response).isNotNull();
            softly.assertThat(detailDto.isOpened()).isFalse();
            softly.assertThat(detailDto.title()).isNotBlank();
            softly.assertThat(detailDto.content()).isNotBlank();
            softly.assertThat(detailDto.images()).isNotBlank();
            softly.assertThat(detailDto.videos()).isNotBlank();
            softly.assertThat(members.size()).isEqualTo(groupMemberCount);
        });
    }

    @Test
    void 개봉일이_지나고_개봉되지_않은_캡슐을_조회하면_상세_내용을_볼_수_없다() {
        //given
        given(
            groupCapsuleQueryRepository.findGroupCapsuleDetailDtoByCapsuleId(anyLong())).willReturn(
            CapsuleDtoFixture.getGroupCapsuleDetailDto(capsuleId, false,
                ZonedDateTime.now().minusDays(5), 3)
        );

        //when
        GroupCapsuleDetailDto response = groupCapsuleService.findGroupCapsuleDetailByGroupIdAndCapsuleId(
            capsuleId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            CapsuleDetailDto detailDto = response.capsuleDetailDto();
            List<GroupMemberSummaryDto> members = response.members();
            softly.assertThat(response).isNotNull();
            softly.assertThat(detailDto.isOpened()).isFalse();
            softly.assertThat(detailDto.images()).isNullOrEmpty();
            softly.assertThat(detailDto.videos()).isNullOrEmpty();
            softly.assertThat(members.size()).isEqualTo(groupMemberCount);
        });
    }

    @Test
    void 개봉일이_지나지_않고_개봉되지_않은_캡슐을_조회하면_상세_내용을_볼_수_없다() {
        //given
        given(
            groupCapsuleQueryRepository.findGroupCapsuleDetailDtoByCapsuleId(anyLong())).willReturn(
            CapsuleDtoFixture.getGroupCapsuleDetailDto(capsuleId, false,
                ZonedDateTime.now().plusDays(5), 3)
        );

        //when
        GroupCapsuleDetailDto response = groupCapsuleService.findGroupCapsuleDetailByGroupIdAndCapsuleId(
            capsuleId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            CapsuleDetailDto detailDto = response.capsuleDetailDto();
            List<GroupMemberSummaryDto> members = response.members();
            softly.assertThat(response).isNotNull();
            softly.assertThat(detailDto.isOpened()).isFalse();
            softly.assertThat(detailDto.images()).isNullOrEmpty();
            softly.assertThat(detailDto.videos()).isNullOrEmpty();
            softly.assertThat(members.size()).isEqualTo(groupMemberCount);
        });
    }

    @Test
    void 개봉일이_지나지_않고_개봉된_캡슐을_조회하면_상세_내용을_볼_수_없다() {
        //given
        given(
            groupCapsuleQueryRepository.findGroupCapsuleDetailDtoByCapsuleId(anyLong())).willReturn(
            CapsuleDtoFixture.getGroupCapsuleDetailDto(capsuleId, true,
                ZonedDateTime.now().plusDays(5), 3)
        );

        //when
        GroupCapsuleDetailDto response = groupCapsuleService.findGroupCapsuleDetailByGroupIdAndCapsuleId(
            capsuleId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            CapsuleDetailDto detailDto = response.capsuleDetailDto();
            List<GroupMemberSummaryDto> members = response.members();
            softly.assertThat(response).isNotNull();
            softly.assertThat(detailDto.isOpened()).isTrue();
            softly.assertThat(detailDto.images()).isNullOrEmpty();
            softly.assertThat(detailDto.videos()).isNullOrEmpty();
            softly.assertThat(members.size()).isEqualTo(groupMemberCount);
        });
    }

    @Test
    void 그룹_캡슐_개봉이_없는_캡슐을_개봉하려는_경우_예외가_발생한다() {
        //given
        Long memberId = 1L;
        Long capsuleId = 1L;
        given(capsuleRepository.findGroupCapsuleByMemberIdAndCapsuleId(anyLong(), anyLong()))
            .willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> groupCapsuleService.openGroupCapsule(memberId, capsuleId))
            .isInstanceOf(CapsuleNotFondException.class)
            .hasMessageContaining(ErrorCode.CAPSULE_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    void 모든_그룹원이_캡슐을_개봉하지_않은_경우_캡슐은_개봉되지_않는다() {
        //given
        Long memberId = 1L;
        Long capsuleId = 1L;

        Optional<Capsule> groupCapsule = getGroupCapsule(memberId, capsuleId);
        given(capsuleRepository.findGroupCapsuleByMemberIdAndCapsuleId(anyLong(), anyLong()))
            .willReturn(groupCapsule);

        List<Member> groupMembers = MemberFixture.membersWithMemberId(memberId.intValue() + 2, 4);
        given(groupCapsuleOpenRepository.findByCapsuleId(capsuleId))
            .willReturn(
                GroupCapsuleOpenFixture.groupCapsuleOpens(false, groupCapsule.get(), groupMembers)
            );

        //when
        groupCapsuleService.openGroupCapsule(memberId, capsuleId);

        //then
        assertThat(groupCapsule.get().getIsOpened()).isFalse();
    }

    private Optional<Capsule> getGroupCapsule(Long memberId, Long capsuleId) {
        Member member = MemberFixture.memberWithMemberId(memberId);

        return Optional.ofNullable(
            CapsuleFixture.groupCapsuleWithCapsuleId(
                member,
                CapsuleSkinFixture.capsuleSkin(member),
                GroupFixture.group(),
                capsuleId
            )
        );
    }

    @Test
    void 모든_그룹원이_캡슐을_개봉한_경우_캡슐은_개봉된다() {
        //given
        Long memberId = 1L;
        Long capsuleId = 1L;

        Optional<Capsule> groupCapsule = getGroupCapsule(memberId, capsuleId);
        given(capsuleRepository.findGroupCapsuleByMemberIdAndCapsuleId(anyLong(), anyLong()))
            .willReturn(groupCapsule);

        List<Member> groupMembers = MemberFixture.membersWithMemberId(memberId.intValue() + 2, 4);
        given(groupCapsuleOpenRepository.findByCapsuleId(capsuleId))
            .willReturn(
                GroupCapsuleOpenFixture.groupCapsuleOpens(true, groupCapsule.get(), groupMembers)
            );

        //when
        groupCapsuleService.openGroupCapsule(memberId, capsuleId);

        //then
        assertThat(groupCapsule.get().getIsOpened()).isTrue();
    }

    @Test
    void 일부_그룹원이_캡슐을_개봉한_경우_캡슐은_개봉되지_않는다() {
        //given
        Long memberId = 1L;
        Long capsuleId = 1L;

        Optional<Capsule> groupCapsule = getGroupCapsule(memberId, capsuleId);
        given(capsuleRepository.findGroupCapsuleByMemberIdAndCapsuleId(anyLong(), anyLong()))
            .willReturn(groupCapsule);

        List<Member> groupMembers = MemberFixture.membersWithMemberId(memberId.intValue() + 2, 4);
        given(groupCapsuleOpenRepository.findByCapsuleId(capsuleId))
            .willReturn(
                GroupCapsuleOpenFixture.groupCapsuleOpensNotAllOpened(groupCapsule.get(), groupMembers)
            );

        //when
        groupCapsuleService.openGroupCapsule(memberId, capsuleId);

        //then
        assertThat(groupCapsule.get().getIsOpened()).isFalse();
    }

    @Test
    void 모든_그룹원이_캡슐을_개봉이_없는_경우_예외가_발생한다() {
        //given
        Long memberId = 1L;
        Long capsuleId = 1L;

        Optional<Capsule> groupCapsule = getGroupCapsule(memberId, capsuleId);
        given(capsuleRepository.findGroupCapsuleByMemberIdAndCapsuleId(anyLong(), anyLong()))
            .willReturn(groupCapsule);
        given(groupCapsuleOpenRepository.findByCapsuleId(capsuleId)).willReturn(
            Collections.emptyList());

        //when
        //then
        assertThatThrownBy(() -> groupCapsuleService.openGroupCapsule(memberId, capsuleId))
            .isInstanceOf(GroupCapsuleOpenNotFoundException.class)
            .hasMessageContaining(ErrorCode.GROUP_CAPSULE_OPEN_NOT_FOUND_ERROR.getMessage());
    }
}
