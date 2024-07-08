package site.timecapsulearchive.core.domain.capsule.media.repository;


import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleFixture;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleSkinFixture;
import site.timecapsulearchive.core.common.fixture.domain.ImageFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.domain.VideoFixture;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.entity.Image;
import site.timecapsulearchive.core.domain.capsule.entity.Video;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.image.ImageQueryRepository;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.image.ImageQueryRepositoryImpl;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.video.VideoQueryRepository;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.video.VideoQueryRepositoryImpl;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.member.entity.Member;

@TestConstructor(autowireMode = AutowireMode.ALL)
class MediaQueryRepositoryTest extends RepositoryTest {

    private final JdbcTemplate jdbcTemplate;
    private final ImageQueryRepository imageQueryRepository;
    private final VideoQueryRepository videoQueryRepository;
    private List<Image> images;
    private List<Video> videos;
    private Capsule capsule;
    private Member member;

    public MediaQueryRepositoryTest(
        JdbcTemplate jdbcTemplate,
        DataSource dataSource
    ) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.imageQueryRepository = new ImageQueryRepositoryImpl(jdbcTemplate);
        this.videoQueryRepository = new VideoQueryRepositoryImpl(jdbcTemplate);
    }

    @Transactional
    @BeforeEach
    void setUp(@Autowired EntityManager entityManager) {
        member = MemberFixture.member(1);
        entityManager.persist(member);

        CapsuleSkin capsuleSkin = CapsuleSkinFixture.capsuleSkin(member);
        entityManager.persist(capsuleSkin);

        capsule = CapsuleFixture.capsule(member, capsuleSkin, CapsuleType.PUBLIC);
        entityManager.persist(capsule);

        images = ImageFixture.testImages(member, capsule, 5);
        videos = VideoFixture.testVideos(member, capsule, 1);
    }

    @Test
    void 이미지들의_데이터가_개수대로_벌크_저장이_되는지_확인한다() {
        //given
        //when
        imageQueryRepository.bulkSave(images);

        //then
        List<Image> actualImages = jdbcTemplate.query(
            """
                SELECT * from image WHERE capsule_id = ?
                """,
            new Object[]{capsule.getId()},
            (rs, rowNum) -> Image.builder()
                .imageUrl(rs.getString("image_url"))
                .member(member)
                .capsule(capsule)
                .build()
        );

        assertThat(actualImages.size()).isEqualTo(images.size());
    }

    @Test
    void 비디오들의_데이터가_개수대로_벌크_저장이_되는지_확인한다() {
        //given
        //when
        videoQueryRepository.bulkSave(videos);

        //then
        List<Video> actualVideos = jdbcTemplate.query(
            """
                SELECT * from video WHERE capsule_id = ?
                """,
            new Object[]{capsule.getId()},
            (rs, rowNum) -> Video.builder()
                .videoUrl(rs.getString("video_url"))
                .member(member)
                .capsule(capsule)
                .build()
        );

        assertThat(actualVideos.size()).isEqualTo(videos.size());
    }
}



