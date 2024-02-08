package site.timecapsulearchive.core.domain.capsule.mapper;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.Video;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.infra.s3.manager.S3UrlGenerator;

@Component
@RequiredArgsConstructor
public class VideoMapper {

    private final S3UrlGenerator s3UrlGenerator;

    public List<Video> toEntity(
        final Capsule capsule,
        final Member member,
        final String directory,
        final List<String> videoNames
    ) {
        return videoNames.stream()
            .map(videoName -> Video.builder()
                .videoUrl(s3UrlGenerator.generateFileName(member.getId(), directory, videoName))
                .capsule(capsule)
                .member(member)
                .build()
            )
            .toList();
    }
}
