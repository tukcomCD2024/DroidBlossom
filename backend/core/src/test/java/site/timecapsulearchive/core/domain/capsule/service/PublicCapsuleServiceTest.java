package site.timecapsulearchive.core.domain.capsule.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.time.ZonedDateTime;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import site.timecapsulearchive.core.common.dependency.UnitTestDependency;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.repository.PublicCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.CapsuleDetailResponse;
import site.timecapsulearchive.core.global.geography.GeoTransformConfig;
import site.timecapsulearchive.core.infra.s3.config.S3Config;

@Import({S3Config.class, GeoTransformConfig.class})
class PublicCapsuleServiceTest {

    private final PublicCapsuleQueryRepository publicCapsuleQueryRepository = mock(
        PublicCapsuleQueryRepository.class);
    private final CapsuleMapper capsuleMapper;
    private final PublicCapsuleService publicCapsuleService;

    PublicCapsuleServiceTest() {
        this.capsuleMapper = new CapsuleMapper(UnitTestDependency.geoTransformManager(),
            UnitTestDependency.s3PreSignedUrlManager());
        this.publicCapsuleService = new PublicCapsuleService(publicCapsuleQueryRepository,
            capsuleMapper);
    }

    @Test
    void 개봉된_캡슐을_조회하면_모든_내용을_볼_수_있다() {
        //given
        Long memberId = 1L;
        Long capsuleId = 1L;
        given(publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
            anyLong(), anyLong()))
            .willReturn(getCapsuleDetailDto(capsuleId, true, ZonedDateTime.now()));

        //when
        CapsuleDetailResponse response = publicCapsuleService.findPublicCapsuleDetailByMemberIdAndCapsuleId(
            memberId, capsuleId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).isNotNull();
            softly.assertThat(response.title()).isNotBlank();
            softly.assertThat(response.content()).isNotBlank();
            softly.assertThat(response.imageUrls()).allMatch(urls -> !urls.isBlank());
            softly.assertThat(response.videoUrls()).allMatch(urls -> !urls.isBlank());
        });
    }

    private Optional<CapsuleDetailDto> getCapsuleDetailDto(Long capsuleId, Boolean isOpened,
        ZonedDateTime dueDate) {
        ZonedDateTime now = ZonedDateTime.now();

        return Optional.of(
            new CapsuleDetailDto(capsuleId, "test", dueDate, "test", "test", now, "address",
                "roadName", "title", "content", "images", "videos", isOpened, CapsuleType.PUBLIC)
        );
    }

    @Test
    void 개봉일이_없고_개봉된_캡슐을_조회하면_모든_내용을_볼_수_있다() {
        //given
        Long memberId = 1L;
        Long capsuleId = 1L;
        given(publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
            anyLong(), anyLong()))
            .willReturn(getCapsuleDetailDto(capsuleId, true, null));

        //when
        CapsuleDetailResponse response = publicCapsuleService.findPublicCapsuleDetailByMemberIdAndCapsuleId(
            memberId, capsuleId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).isNotNull();
            softly.assertThat(response.title()).isNotBlank();
            softly.assertThat(response.content()).isNotBlank();
            softly.assertThat(response.imageUrls()).allMatch(urls -> !urls.isBlank());
            softly.assertThat(response.videoUrls()).allMatch(urls -> !urls.isBlank());
        });
    }

    @Test
    void 개봉일이_없고_개봉되지_않은_캡슐을_조회하면_모든_내용을_볼_수_있다() {
        //given
        Long memberId = 1L;
        Long capsuleId = 1L;
        given(publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
            anyLong(), anyLong()))
            .willReturn(getCapsuleDetailDto(capsuleId, false, null));

        //when
        CapsuleDetailResponse response = publicCapsuleService.findPublicCapsuleDetailByMemberIdAndCapsuleId(
            memberId, capsuleId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).isNotNull();
            softly.assertThat(response.title()).isNotBlank();
            softly.assertThat(response.content()).isNotBlank();
            softly.assertThat(response.imageUrls()).allMatch(urls -> !urls.isBlank());
            softly.assertThat(response.videoUrls()).allMatch(urls -> !urls.isBlank());
        });
    }

    @Test
    void 개봉일이_지난_개봉되지_않은_캡슐을_조회하면_모든_내용을_볼_수_없다() {
        //given
        Long memberId = 1L;
        Long capsuleId = 1L;
        given(publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
            anyLong(), anyLong()))
            .willReturn(getCapsuleDetailDto(capsuleId, false, ZonedDateTime.now().minusDays(5)));

        //when
        CapsuleDetailResponse response = publicCapsuleService.findPublicCapsuleDetailByMemberIdAndCapsuleId(
            memberId, capsuleId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).isNotNull();
            softly.assertThat(response.isOpened()).isFalse();
            softly.assertThat(response.imageUrls()).isEmpty();
            softly.assertThat(response.videoUrls()).isEmpty();
        });
    }

    @Test
    void 개봉일이_지나지_않고_개봉되지_않은_캡슐을_조회하면_모든_내용을_볼_수_없다() {
        //given
        Long memberId = 1L;
        Long capsuleId = 1L;
        given(publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
            anyLong(), anyLong()))
            .willReturn(getCapsuleDetailDto(capsuleId, false, ZonedDateTime.now().plusDays(5)));

        //when
        CapsuleDetailResponse response = publicCapsuleService.findPublicCapsuleDetailByMemberIdAndCapsuleId(
            memberId, capsuleId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).isNotNull();
            softly.assertThat(response.isOpened()).isFalse();
            softly.assertThat(response.imageUrls()).isEmpty();
            softly.assertThat(response.videoUrls()).isEmpty();
        });
    }

    @Test
    void 개봉일이_지나지_않고_개봉된_캡슐을_조회하면_모든_내용을_볼_수_없다() {
        //given
        Long memberId = 1L;
        Long capsuleId = 1L;
        given(publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
            anyLong(), anyLong()))
            .willReturn(getCapsuleDetailDto(capsuleId, true, ZonedDateTime.now().plusDays(5)));

        //when
        CapsuleDetailResponse response = publicCapsuleService.findPublicCapsuleDetailByMemberIdAndCapsuleId(
            memberId, capsuleId);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).isNotNull();
            softly.assertThat(response.isOpened()).isTrue();
            softly.assertThat(response.imageUrls()).isEmpty();
            softly.assertThat(response.videoUrls()).isEmpty();
        });
    }
}
