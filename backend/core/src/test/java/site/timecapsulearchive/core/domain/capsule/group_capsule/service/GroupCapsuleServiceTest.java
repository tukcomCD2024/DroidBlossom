package site.timecapsulearchive.core.domain.capsule.group_capsule.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.dto.CapsuleBasicInfoDtoFixture;
import site.timecapsulearchive.core.common.fixture.dto.CapsuleDtoFixture;
import site.timecapsulearchive.core.domain.capsule.data.dto.CapsuleBasicInfoDto;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.exception.GroupCapsuleOpenNotFoundException;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.CapsuleRepository;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.CapsuleOpenStatus;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleOpenStateDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleOpenQueryRepository;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleSliceRequestDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberSummaryDto;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member_group.exception.NoGroupAuthorityException;
import site.timecapsulearchive.core.domain.member_group.repository.member_group_repository.MemberGroupRepository;
import site.timecapsulearchive.core.global.error.ErrorCode;

class GroupCapsuleServiceTest {

    private final Long capsuleId = 1L;
    private final Long memberId = 1L;
    private final int groupMemberCount = 3;

    private final CapsuleRepository capsuleRepository = mock(CapsuleRepository.class);
    private final GroupCapsuleQueryRepository groupCapsuleQueryRepository = mock(
        GroupCapsuleQueryRepository.class);
    private final GroupCapsuleOpenQueryRepository groupCapsuleOpenQueryRepository = mock(
        GroupCapsuleOpenQueryRepository.class);
    private final MemberGroupRepository memberGroupRepository = mock(MemberGroupRepository.class);

    private final GroupCapsuleService groupCapsuleService = new GroupCapsuleService(
        capsuleRepository, groupCapsuleQueryRepository, groupCapsuleOpenQueryRepository,
        memberGroupRepository);

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
        assertSoftly(softly -> {
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
        assertSoftly(softly -> {
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
        assertSoftly(softly -> {
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
        assertSoftly(softly -> {
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
        assertSoftly(softly -> {
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
        assertSoftly(softly -> {
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
    void 그룹_캡슐이_없는_경우_그룹_캡슐_개봉_시_예외가_발생한다() {
        //given
        given(
            capsuleRepository.findNotOpenedGroupCapsuleByMemberIdAndCapsuleId(anyLong(), anyLong()))
            .willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> groupCapsuleService.openGroupCapsule(memberId, capsuleId))
            .isInstanceOf(CapsuleNotFondException.class)
            .hasMessageContaining(ErrorCode.CAPSULE_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    void 개봉일이_지나지_않아_그룹_캡슐을_열_수_없는_경우_캡슐은_개봉되지_않는다() {
        //given
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        Optional<Capsule> groupCapsule = CapsuleFixture.groupCapsuleSpecificTime(memberId,
            capsuleId, now.plusYears(5));
        given(
            capsuleRepository.findNotOpenedGroupCapsuleByMemberIdAndCapsuleId(anyLong(), anyLong()))
            .willReturn(groupCapsule);

        //when
        GroupCapsuleOpenStateDto groupCapsuleOpenStateDto = groupCapsuleService.openGroupCapsule(
            memberId, capsuleId);

        //then
        assertSoftly(softly -> {
            softly.assertThat(groupCapsule.get().getIsOpened()).isFalse();
            softly.assertThat(groupCapsuleOpenStateDto.capsuleOpenStatus())
                .isEqualTo(CapsuleOpenStatus.NOT_OPEN);
            softly.assertThat(groupCapsuleOpenStateDto.isIndividuallyOpened()).isFalse();
        });
    }

    @Test
    void 타임캡슐이_아닌_경우_그룹_캡슐_개봉_시_그룹_캡슐은_개봉된다() {
        //given
        Optional<Capsule> groupCapsule = CapsuleFixture.groupCapsuleSpecificTime(memberId,
            capsuleId, null);
        given(
            capsuleRepository.findNotOpenedGroupCapsuleByMemberIdAndCapsuleId(anyLong(), anyLong()))
            .willReturn(groupCapsule);

        //when
        GroupCapsuleOpenStateDto groupCapsuleOpenStateDto = groupCapsuleService.openGroupCapsule(
            memberId, capsuleId);

        //then
        assertSoftly(softly -> {
            softly.assertThat(groupCapsule.get().getIsOpened()).isTrue();
            softly.assertThat(groupCapsuleOpenStateDto.capsuleOpenStatus())
                .isEqualTo(CapsuleOpenStatus.OPEN);
            softly.assertThat(groupCapsuleOpenStateDto.isIndividuallyOpened()).isTrue();
        });
    }

    @Test
    void 모든_그룹원이_캡슐을_개봉하지_않은_경우_캡슐은_개봉되지_않는다() {
        //given
        List<Member> groupMembers = MemberFixture.membersWithMemberId(memberId.intValue(), 4);
        Optional<Capsule> groupCapsule = CapsuleFixture.groupCapsuleNotAllMemberOpen(memberId,
            capsuleId,
            groupMembers);
        given(
            capsuleRepository.findNotOpenedGroupCapsuleByMemberIdAndCapsuleId(anyLong(), anyLong()))
            .willReturn(groupCapsule);

        //when
        GroupCapsuleOpenStateDto groupCapsuleOpenStateDto = groupCapsuleService.openGroupCapsule(
            memberId, capsuleId);

        //then
        assertSoftly(softly -> {
            softly.assertThat(groupCapsule.get().getIsOpened()).isFalse();
            softly.assertThat(groupCapsuleOpenStateDto.capsuleOpenStatus()).isEqualTo(
                CapsuleOpenStatus.NOT_OPEN);
            softly.assertThat(groupCapsuleOpenStateDto.isIndividuallyOpened()).isTrue();
        });
    }

    @Test
    void 일부_그룹원이_캡슐을_개봉한_경우_캡슐은_개봉되지_않는다() {
        //given
        List<Member> groupMembers = MemberFixture.membersWithMemberId(memberId.intValue(), 4);
        Optional<Capsule> groupCapsule = CapsuleFixture.groupCapsuleHalfMemberOpen(memberId,
            capsuleId,
            groupMembers);
        given(
            capsuleRepository.findNotOpenedGroupCapsuleByMemberIdAndCapsuleId(anyLong(), anyLong()))
            .willReturn(groupCapsule);

        //when
        GroupCapsuleOpenStateDto groupCapsuleOpenStateDto = groupCapsuleService.openGroupCapsule(
            memberId, capsuleId);

        //then
        assertSoftly(softly -> {
            softly.assertThat(groupCapsule.get().getIsOpened()).isFalse();
            softly.assertThat(groupCapsuleOpenStateDto.capsuleOpenStatus()).isEqualTo(
                CapsuleOpenStatus.NOT_OPEN);
            softly.assertThat(groupCapsuleOpenStateDto.isIndividuallyOpened()).isTrue();
        });
    }

    @Test
    void 그룹_캡슐_개봉이_없는_경우_예외가_발생한다() {
        //given
        Optional<Capsule> groupCapsule = CapsuleFixture.groupCapsuleEmptyOpen(memberId, capsuleId);
        given(
            capsuleRepository.findNotOpenedGroupCapsuleByMemberIdAndCapsuleId(anyLong(), anyLong()))
            .willReturn(groupCapsule);

        //when
        //then
        assertThatThrownBy(() -> groupCapsuleService.openGroupCapsule(memberId, capsuleId))
            .isInstanceOf(GroupCapsuleOpenNotFoundException.class)
            .hasMessageContaining(ErrorCode.GROUP_CAPSULE_OPEN_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    void 모든_그룹원이_캡슐을_개봉한_경우_캡슐은_개봉된다() {
        //given
        List<Member> groupMembers = MemberFixture.membersWithMemberId(memberId.intValue(), 4);
        Optional<Capsule> groupCapsule = CapsuleFixture.groupCapsuleAllMemberOpen(memberId,
            capsuleId,
            groupMembers);
        given(
            capsuleRepository.findNotOpenedGroupCapsuleByMemberIdAndCapsuleId(anyLong(), anyLong()))
            .willReturn(groupCapsule);

        //when
        GroupCapsuleOpenStateDto groupCapsuleOpenStateDto = groupCapsuleService.openGroupCapsule(
            memberId, capsuleId);

        //then
        assertSoftly(softly -> {
            softly.assertThat(groupCapsule.get().getIsOpened()).isTrue();
            softly.assertThat(groupCapsuleOpenStateDto.capsuleOpenStatus()).isEqualTo(
                CapsuleOpenStatus.OPEN);
            softly.assertThat(groupCapsuleOpenStateDto.isIndividuallyOpened()).isTrue();
        });
    }

    @Test
    void 사용자를_제외한_그룹원이_캡슐을_개봉한_경우_그룹_캡슐_개봉을_시도하면_캡슐은_개봉된다() {
        //given
        List<Member> groupMembers = MemberFixture.membersWithMemberId(memberId.intValue(), 4);
        Optional<Capsule> groupCapsule = CapsuleFixture.groupCapsuleExcludeSpecificMember(memberId,
            capsuleId,
            groupMembers);
        given(
            capsuleRepository.findNotOpenedGroupCapsuleByMemberIdAndCapsuleId(anyLong(), anyLong()))
            .willReturn(groupCapsule);

        //when
        GroupCapsuleOpenStateDto groupCapsuleOpenStateDto = groupCapsuleService.openGroupCapsule(
            memberId, capsuleId);

        //then
        assertSoftly(softly -> {
            softly.assertThat(groupCapsule.get().getIsOpened()).isTrue();
            softly.assertThat(groupCapsuleOpenStateDto.capsuleOpenStatus()).isEqualTo(
                CapsuleOpenStatus.OPEN);
            softly.assertThat(groupCapsuleOpenStateDto.isIndividuallyOpened()).isTrue();
        });
    }

    @Test
    void 그룹원이_아닌_사용자가_그룹_캡슐_개봉_상태를_조회하면_오류가_발생한다() throws Exception {
        //given
        Long groupId = 1L;

        //when
        //then
        assertThatThrownBy(
            () -> groupCapsuleService.findGroupMemberCapsuleOpenStatus(memberId, capsuleId,
                groupId))
            .isInstanceOf(NoGroupAuthorityException.class)
            .hasMessageContaining(ErrorCode.NO_GROUP_AUTHORITY_ERROR.getMessage());
    }

    @Test
    void 그룹원이_그룹_캡슐_개봉_상태를_조회하면_그룹_캡슐_개봉_상태를_조회할_수_있다() throws Exception {
        //given
        Long groupId = 1L;
        int size = 20;
        given(memberGroupRepository.existMemberGroupByMemberIdAndGroupId(memberId, groupId))
            .willReturn(true);
        given(groupCapsuleOpenQueryRepository.findGroupMemberCapsuleOpenStatus(capsuleId, groupId))
            .willReturn(
                GroupMemberCapsuleOpenStatusDtoFixture.groupMemberCapsuleOpenStatusDto(memberId,
                    size));

        //when
        List<GroupMemberCapsuleOpenStatusDto> groupMemberCapsuleOpenStatus = groupCapsuleService.findGroupMemberCapsuleOpenStatus(
            memberId, capsuleId, groupId);

        //then
        assertThat(groupMemberCapsuleOpenStatus).isNotEmpty();
    }

    @Test
    void 사용자가_그룹원이_아니면_그룹_캡슐_목록_조회_시_예외가_발생한다() {
        //given
        Long groupId = 1L;
        int size = 20;
        GroupCapsuleSliceRequestDto dto = GroupCapsuleSliceRequestDto.createOf(memberId, groupId,
            size, capsuleId);
        given(memberGroupRepository.existMemberGroupByMemberIdAndGroupId(memberId, groupId))
            .willReturn(false);

        //when
        //then
        assertThatThrownBy(() -> groupCapsuleService.findGroupCapsuleSlice(dto))
            .isInstanceOf(NoGroupAuthorityException.class)
            .hasMessageContaining(ErrorCode.NO_GROUP_AUTHORITY_ERROR.getMessage());
    }

    @Test
    void 사용자가_그룹원이면_그룹_캡슐_목록_조회_시_그룹_캡슐_목록이_조회된다() {
        //given
        Long groupId = 1L;
        int size = 20;
        GroupCapsuleSliceRequestDto dto = GroupCapsuleSliceRequestDto.createOf(memberId, groupId,
            size, capsuleId);
        given(memberGroupRepository.existMemberGroupByMemberIdAndGroupId(memberId, groupId))
            .willReturn(true);
        given(groupCapsuleQueryRepository.findGroupCapsuleSlice(
            any(GroupCapsuleSliceRequestDto.class)))
            .willReturn(
                new SliceImpl<>(CapsuleBasicInfoDtoFixture.capsuleBasicInfoDtos(capsuleId, size)));

        //when
        Slice<CapsuleBasicInfoDto> groupCapsuleSlice = groupCapsuleService.findGroupCapsuleSlice(
            dto);

        //then
        assertThat(groupCapsuleSlice.hasContent()).isTrue();
    }
}
