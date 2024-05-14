package site.timecapsulearchive.core.domain.capsule.group_capsule.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.time.ZonedDateTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import site.timecapsulearchive.core.common.fixture.dto.CapsuleDtoFixture;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.CapsuleRepository;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.capsule.group_capsule.service.GroupCapsuleService;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberSummaryDto;

class GroupCapsuleServiceTest {

    private final Long capsuleId = 1L;
    private final int groupMemberCount = 3;

    private final GroupCapsuleQueryRepository groupCapsuleQueryRepository = mock(
        GroupCapsuleQueryRepository.class);
    private final GroupCapsuleService groupCapsuleService;

    public GroupCapsuleServiceTest() {
        CapsuleRepository capsuleRepository = mock(CapsuleRepository.class);
        this.groupCapsuleService = new GroupCapsuleService(capsuleRepository, groupCapsuleQueryRepository);
    }

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
            CapsuleDtoFixture.getGroupCapsuleDetailDto(capsuleId, false, ZonedDateTime.now().minusDays(5), 3)
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
            CapsuleDtoFixture.getGroupCapsuleDetailDto(capsuleId, false, ZonedDateTime.now().plusDays(5), 3)
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
            CapsuleDtoFixture.getGroupCapsuleDetailDto(capsuleId, true, ZonedDateTime.now().plusDays(5), 3)
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

}
