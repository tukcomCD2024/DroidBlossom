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
import site.timecapsulearchive.core.common.fixture.dto.GroupCapsuleMemberDtoFixture;
import site.timecapsulearchive.core.common.fixture.dto.GroupCapsuleSummaryDtoFixture;
import site.timecapsulearchive.core.domain.capsule.data.dto.CapsuleBasicInfoDto;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.exception.GroupCapsuleOpenNotFoundException;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.capsule.CapsuleRepository;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.image.ImageRepository;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.video.VideoRepository;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.CapsuleOpenStatus;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.CombinedGroupCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleMemberDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleMemberSummaryDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleOpenStateDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupSpecificCapsuleSliceRequestDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleOpenRepository;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleQueryRepository;
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
    private final GroupCapsuleOpenRepository groupCapsuleOpenRepository = mock(
        GroupCapsuleOpenRepository.class);
    private final ImageRepository imageRepository = mock(ImageRepository.class);
    private final VideoRepository videoRepository = mock(VideoRepository.class);
    private final MemberGroupRepository memberGroupRepository = mock(MemberGroupRepository.class);

    private final GroupCapsuleService groupCapsuleService = new GroupCapsuleService(
        capsuleRepository, groupCapsuleQueryRepository, groupCapsuleOpenRepository, imageRepository,
        videoRepository, memberGroupRepository);

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
            List<GroupCapsuleMemberSummaryDto> members = response.members();
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
            List<GroupCapsuleMemberSummaryDto> members = response.members();
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
            List<GroupCapsuleMemberSummaryDto> members = response.members();
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
            List<GroupCapsuleMemberSummaryDto> members = response.members();
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
            List<GroupCapsuleMemberSummaryDto> members = response.members();
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
            List<GroupCapsuleMemberSummaryDto> members = response.members();
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
            capsuleRepository.findNotOpenedGroupCapsuleByMemberIdAndCapsuleId(anyLong()))
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
            capsuleRepository.findNotOpenedGroupCapsuleByMemberIdAndCapsuleId(anyLong()))
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
            capsuleRepository.findNotOpenedGroupCapsuleByMemberIdAndCapsuleId(anyLong()))
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
            capsuleRepository.findNotOpenedGroupCapsuleByMemberIdAndCapsuleId(anyLong()))
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
            capsuleRepository.findNotOpenedGroupCapsuleByMemberIdAndCapsuleId(anyLong()))
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
            capsuleRepository.findNotOpenedGroupCapsuleByMemberIdAndCapsuleId(anyLong()))
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
            capsuleRepository.findNotOpenedGroupCapsuleByMemberIdAndCapsuleId(anyLong()))
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
            capsuleRepository.findNotOpenedGroupCapsuleByMemberIdAndCapsuleId(anyLong()))
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
    void 그룹원이_아닌_사용자가_그룹_캡슐_개봉_상태를_조회하면_오류가_발생한다() {
        //given
        Long groupId = 1L;
        Long notGroupMemberId = 100L;
        int size = 20;
        given(groupCapsuleOpenRepository.findGroupCapsuleMembers(capsuleId, groupId))
            .willReturn(GroupCapsuleMemberDtoFixture.members(memberId.intValue(), size, false));

        //when
        //then
        assertThatThrownBy(
            () -> groupCapsuleService.findGroupCapsuleMembers(notGroupMemberId, capsuleId,
                groupId))
            .isInstanceOf(NoGroupAuthorityException.class)
            .hasMessageContaining(ErrorCode.NO_GROUP_AUTHORITY_ERROR.getMessage());
    }

    @Test
    void 그룹원이_그룹_캡슐_개봉_상태를_조회하면_그룹_캡슐_개봉_상태를_조회할_수_있다() {
        //given
        Long groupId = 1L;
        int size = 20;
        given(groupCapsuleOpenRepository.findGroupCapsuleMembers(capsuleId, groupId))
            .willReturn(GroupCapsuleMemberDtoFixture.members(memberId.intValue(), size, false));

        //when
        List<GroupCapsuleMemberDto> groupCapsuleMembers = groupCapsuleService.findGroupCapsuleMembers(
            memberId, capsuleId, groupId);

        //then
        assertThat(groupCapsuleMembers).isNotEmpty();
    }

    @Test
    void 사용자가_그룹원이_아니면_그룹_캡슐_목록_조회_시_예외가_발생한다() {
        //given
        Long groupId = 1L;
        int size = 20;
        GroupSpecificCapsuleSliceRequestDto dto = GroupSpecificCapsuleSliceRequestDto.createOf(memberId, groupId,
            size, capsuleId);
        given(memberGroupRepository.existMemberGroupByMemberIdAndGroupId(memberId, groupId))
            .willReturn(false);

        //when
        //then
        assertThatThrownBy(() -> groupCapsuleService.findGroupSpecificCapsuleSlice(dto))
            .isInstanceOf(NoGroupAuthorityException.class)
            .hasMessageContaining(ErrorCode.NO_GROUP_AUTHORITY_ERROR.getMessage());
    }

    @Test
    void 사용자가_그룹원이면_그룹_캡슐_목록_조회_시_그룹_캡슐_목록이_조회된다() {
        //given
        Long groupId = 1L;
        int size = 20;
        GroupSpecificCapsuleSliceRequestDto dto = GroupSpecificCapsuleSliceRequestDto.createOf(memberId, groupId,
            size, capsuleId);
        given(memberGroupRepository.existMemberGroupByMemberIdAndGroupId(memberId, groupId))
            .willReturn(true);
        given(groupCapsuleQueryRepository.findGroupSpecificCapsuleSlice(
            any(GroupSpecificCapsuleSliceRequestDto.class)))
            .willReturn(
                new SliceImpl<>(CapsuleBasicInfoDtoFixture.capsuleBasicInfoDtos(capsuleId, size)));

        //when
        Slice<CapsuleBasicInfoDto> groupCapsuleSlice = groupCapsuleService.findGroupSpecificCapsuleSlice(
            dto);

        //then
        assertThat(groupCapsuleSlice.hasContent()).isTrue();
    }

    @Test
    void 그룹원이_아닌_경우_그룹_캡슐_요약_조회_시_예외가_발생한다() {
        //given
        Long groupId = 1L;
        Long notGroupMemberId = 999L;
        given(groupCapsuleQueryRepository.findGroupCapsuleSummaryDtoByCapsuleId(capsuleId))
            .willReturn(Optional.of(
                GroupCapsuleSummaryDtoFixture.groupCapsule(groupId, memberId)));
        given(memberGroupRepository.findGroupCapsuleMembers(anyLong(), anyLong()))
            .willReturn(GroupCapsuleMemberDtoFixture.members(0, 10, false));

        //when
        //then
        assertThatThrownBy(
            () -> groupCapsuleService.findGroupCapsuleSummary(notGroupMemberId, capsuleId))
            .isInstanceOf(NoGroupAuthorityException.class)
            .hasMessageContaining(ErrorCode.NO_GROUP_AUTHORITY_ERROR.getMessage());
    }

    @Test
    void 그룹원이_존재하지_않는_그룹_캡슐을_요약_조회_시_예외가_발생한다() {
        //given
        given(groupCapsuleQueryRepository.findGroupCapsuleSummaryDtoByCapsuleId(capsuleId))
            .willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(
            () -> groupCapsuleService.findGroupCapsuleSummary(memberId, capsuleId))
            .isInstanceOf(CapsuleNotFondException.class)
            .hasMessageContaining(ErrorCode.CAPSULE_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    void 그룹원이_그룹_캡슐을_요약_조회_시_그룹_캡슐_요약_조회를_볼_수_있다() {
        //given
        Long groupId = 1L;

        given(memberGroupRepository.findGroupCapsuleMembers(anyLong(), anyLong()))
            .willReturn(GroupCapsuleMemberDtoFixture.members(0, 10, false));
        given(groupCapsuleQueryRepository.findGroupCapsuleSummaryDtoByCapsuleId(capsuleId))
            .willReturn(Optional.of(
                GroupCapsuleSummaryDtoFixture.groupCapsule(groupId, memberId)));

        //when
        CombinedGroupCapsuleSummaryDto groupCapsuleSummaryDto = groupCapsuleService.findGroupCapsuleSummary(
            memberId, capsuleId);
        ;

        //then
        assertThat(groupCapsuleSummaryDto).isNotNull();
    }

    @Test
    void 그룹_캡슐을_만든_사람이_아닌_그룹원이_그룹_캡슐을_요약_조회_시_수정_권한이_없다() {
        //given
        Long groupId = 1L;
        Long groupMemberId = 2L;

        given(memberGroupRepository.findGroupCapsuleMembers(anyLong(), anyLong()))
            .willReturn(GroupCapsuleMemberDtoFixture.members(1, 10, false));
        given(groupCapsuleQueryRepository.findGroupCapsuleSummaryDtoByCapsuleId(capsuleId))
            .willReturn(Optional.of(
                GroupCapsuleSummaryDtoFixture.groupCapsule(groupId, memberId)));

        //when
        CombinedGroupCapsuleSummaryDto groupCapsuleSummaryDto = groupCapsuleService.findGroupCapsuleSummary(
            groupMemberId, capsuleId);
        ;

        //then
        assertThat(groupCapsuleSummaryDto.hasEditPermission()).isFalse();
    }

    @Test
    void 그룹_캡슐을_만든_사람이_아닌_그룹원이_그룹_캡슐을_요약_조회_시_삭제_권한이_없다() {
        //given
        Long groupId = 1L;
        Long groupMemberId = 2L;

        given(memberGroupRepository.findGroupCapsuleMembers(anyLong(), anyLong()))
            .willReturn(GroupCapsuleMemberDtoFixture.members(1, 10, false));
        given(groupCapsuleQueryRepository.findGroupCapsuleSummaryDtoByCapsuleId(capsuleId))
            .willReturn(Optional.of(
                GroupCapsuleSummaryDtoFixture.groupCapsule(groupId, memberId)));

        //when
        CombinedGroupCapsuleSummaryDto groupCapsuleSummaryDto = groupCapsuleService.findGroupCapsuleSummary(
            groupMemberId, capsuleId);
        ;

        //then
        assertThat(groupCapsuleSummaryDto.hasDeletePermission()).isFalse();
    }

    @Test
    void 그룹_캡슐을_만든_사람이_그룹_캡슐을_요약_조회_시_수정_권한이_있다() {
        //given
        Long groupId = 1L;

        given(memberGroupRepository.findGroupCapsuleMembers(anyLong(), anyLong()))
            .willReturn(GroupCapsuleMemberDtoFixture.members(1, 10, false));
        given(groupCapsuleQueryRepository.findGroupCapsuleSummaryDtoByCapsuleId(capsuleId))
            .willReturn(Optional.of(
                GroupCapsuleSummaryDtoFixture.groupCapsule(groupId, memberId)));

        //when
        CombinedGroupCapsuleSummaryDto groupCapsuleSummaryDto = groupCapsuleService.findGroupCapsuleSummary(
            memberId, capsuleId);
        ;

        //then
        assertThat(groupCapsuleSummaryDto.hasEditPermission()).isTrue();
    }

    @Test
    void 그룹_캡슐을_만든_사람이_그룹_캡슐을_요약_조회_시_삭제_권한이_있다() {
        //given
        Long groupId = 1L;

        given(memberGroupRepository.findGroupCapsuleMembers(anyLong(), anyLong()))
            .willReturn(GroupCapsuleMemberDtoFixture.members(1, 10, false));
        given(groupCapsuleQueryRepository.findGroupCapsuleSummaryDtoByCapsuleId(capsuleId))
            .willReturn(Optional.of(
                GroupCapsuleSummaryDtoFixture.groupCapsule(groupId, memberId)));

        //when
        CombinedGroupCapsuleSummaryDto groupCapsuleSummaryDto = groupCapsuleService.findGroupCapsuleSummary(
            memberId, capsuleId);
        ;

        //then
        assertThat(groupCapsuleSummaryDto.hasDeletePermission()).isTrue();
    }

    @Test
    void 사용자가_그룹_캡슐을_요약_조회_시_그룹_캡슐을_개봉했으면_현재_사용자의_그룹_캡슐_개봉_여부는_참이다() {
        //given
        Long groupId = 1L;

        given(memberGroupRepository.findGroupCapsuleMembers(anyLong(), anyLong()))
            .willReturn(GroupCapsuleMemberDtoFixture.members(1, 10, true));
        given(groupCapsuleQueryRepository.findGroupCapsuleSummaryDtoByCapsuleId(capsuleId))
            .willReturn(Optional.of(
                GroupCapsuleSummaryDtoFixture.groupCapsule(groupId, memberId)));

        //when
        CombinedGroupCapsuleSummaryDto groupCapsuleSummaryDto = groupCapsuleService.findGroupCapsuleSummary(
            memberId, capsuleId);
        ;

        //then
        assertThat(groupCapsuleSummaryDto.isRequestMemberCapsuleOpen()).isTrue();
    }

    @Test
    void 사용자가_그룹_캡슐을_요약_조회_시_그룹_캡슐을_개봉하지_않았으면_현재_사용자의_그룹_캡슐_개봉_여부는_거짓이다() {
        //given
        Long groupId = 1L;

        given(memberGroupRepository.findGroupCapsuleMembers(anyLong(), anyLong()))
            .willReturn(GroupCapsuleMemberDtoFixture.members(1, 10, false));
        given(groupCapsuleQueryRepository.findGroupCapsuleSummaryDtoByCapsuleId(capsuleId))
            .willReturn(Optional.of(
                GroupCapsuleSummaryDtoFixture.groupCapsule(groupId, memberId)));

        //when
        CombinedGroupCapsuleSummaryDto groupCapsuleSummaryDto = groupCapsuleService.findGroupCapsuleSummary(
            memberId, capsuleId);
        ;

        //then
        assertThat(groupCapsuleSummaryDto.isRequestMemberCapsuleOpen()).isFalse();
    }
}
