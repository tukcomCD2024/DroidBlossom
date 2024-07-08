package site.timecapsulearchive.core.domain.capsule.public_capsule.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.time.ZonedDateTime;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import site.timecapsulearchive.core.common.fixture.dto.CapsuleDtoFixture;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto.PublicCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.public_capsule.repository.PublicCapsuleQueryRepository;
import site.timecapsulearchive.core.global.geography.GeoTransformConfig;
import site.timecapsulearchive.core.infra.s3.config.S3Config;

@Import({S3Config.class, GeoTransformConfig.class})
class PublicCapsuleServiceTest {

    private final Long memberId = 1L;
    private final Long capsuleId = 1L;

    private final PublicCapsuleQueryRepository publicCapsuleQueryRepository = mock(
        PublicCapsuleQueryRepository.class);
    private final PublicCapsuleService publicCapsuleService;

    PublicCapsuleServiceTest() {
        this.publicCapsuleService = new PublicCapsuleService(publicCapsuleQueryRepository);
    }

    @Test
    void 개봉된_캡슐을_조회하면_모든_내용을_볼_수_있다() {
        //given
        given(publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
            anyLong(), anyLong()))
            .willReturn(
                CapsuleDtoFixture.getPublicCapsuleDetailDto(capsuleId, true, ZonedDateTime.now()));

        //when
        PublicCapsuleDetailDto response = publicCapsuleService.findPublicCapsuleDetailByMemberIdAndCapsuleId(
            memberId, capsuleId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).isNotNull();
            softly.assertThat(response.isOpened()).isTrue();
            softly.assertThat(response.title()).isNotBlank();
            softly.assertThat(response.content()).isNotBlank();
            softly.assertThat(response.images()).isNotBlank();
            softly.assertThat(response.videos()).isNotBlank();
        });
    }

    @Test
    void 개봉일이_없고_개봉된_캡슐을_조회하면_모든_내용을_볼_수_있다() {
        //given
        given(publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
            anyLong(), anyLong()))
            .willReturn(CapsuleDtoFixture.getPublicCapsuleDetailDto(capsuleId, true, null));

        //when
        PublicCapsuleDetailDto response = publicCapsuleService.findPublicCapsuleDetailByMemberIdAndCapsuleId(
            memberId, capsuleId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).isNotNull();
            softly.assertThat(response.isOpened()).isTrue();
            softly.assertThat(response.title()).isNotBlank();
            softly.assertThat(response.content()).isNotBlank();
            softly.assertThat(response.images()).isNotBlank();
            softly.assertThat(response.videos()).isNotBlank();
        });
    }

    @Test
    void 개봉일이_없고_개봉되지_않은_캡슐을_조회하면_모든_내용을_볼_수_있다() {
        //given
        given(publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
            anyLong(), anyLong()))
            .willReturn(CapsuleDtoFixture.getPublicCapsuleDetailDto(capsuleId, false, null));

        //when
        PublicCapsuleDetailDto response = publicCapsuleService.findPublicCapsuleDetailByMemberIdAndCapsuleId(
            memberId, capsuleId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).isNotNull();
            softly.assertThat(response.isOpened()).isFalse();
            softly.assertThat(response.title()).isNotBlank();
            softly.assertThat(response.content()).isNotBlank();
            softly.assertThat(response.images()).isNotBlank();
            softly.assertThat(response.videos()).isNotBlank();
        });
    }

    @Test
    void 개봉일이_지나고_개봉되지_않은_캡슐을_조회하면_모든_내용을_볼_수_없다() {
        //given
        given(publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
            anyLong(), anyLong()))
            .willReturn(CapsuleDtoFixture.getPublicCapsuleDetailDto(capsuleId, false,
                ZonedDateTime.now().minusDays(5)));

        //when
        PublicCapsuleDetailDto response = publicCapsuleService.findPublicCapsuleDetailByMemberIdAndCapsuleId(
            memberId, capsuleId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).isNotNull();
            softly.assertThat(response.isOpened()).isFalse();
            softly.assertThat(response.images()).isNullOrEmpty();
            softly.assertThat(response.videos()).isNullOrEmpty();
        });
    }

    @Test
    void 개봉일이_지나지_않고_개봉되지_않은_캡슐을_조회하면_모든_내용을_볼_수_없다() {
        //given
        given(publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
            anyLong(), anyLong()))
            .willReturn(CapsuleDtoFixture.getPublicCapsuleDetailDto(capsuleId, false,
                ZonedDateTime.now().plusDays(5)));

        //when
        PublicCapsuleDetailDto response = publicCapsuleService.findPublicCapsuleDetailByMemberIdAndCapsuleId(
            memberId, capsuleId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).isNotNull();
            softly.assertThat(response.isOpened()).isFalse();
            softly.assertThat(response.images()).isNullOrEmpty();
            softly.assertThat(response.videos()).isNullOrEmpty();
        });
    }

    @Test
    void 개봉일이_지나지_않고_개봉된_캡슐을_조회하면_모든_내용을_볼_수_없다() {
        //given
        given(publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
            anyLong(), anyLong()))
            .willReturn(CapsuleDtoFixture.getPublicCapsuleDetailDto(capsuleId, true,
                ZonedDateTime.now().plusDays(5)));

        //when
        PublicCapsuleDetailDto response = publicCapsuleService.findPublicCapsuleDetailByMemberIdAndCapsuleId(
            memberId, capsuleId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).isNotNull();
            softly.assertThat(response.isOpened()).isTrue();
            softly.assertThat(response.images()).isNullOrEmpty();
            softly.assertThat(response.videos()).isNullOrEmpty();
        });
    }
}
