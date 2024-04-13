package site.timecapsulearchive.core.domain.capsule.generic_capsule.mapper;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.Image;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.infra.s3.manager.S3UrlGenerator;

@Component
@RequiredArgsConstructor
public class ImageMapper {

    private final S3UrlGenerator s3UrlGenerator;

    public List<Image> toEntity(
        final Capsule createCapsule,
        final Member findMember,
        final String directory,
        final List<String> imageNames
    ) {
        return imageNames.stream()
            .map(imageName -> Image.builder()
                .imageUrl(s3UrlGenerator.generateFileName(findMember.getId(), directory, imageName))
                .capsule(createCapsule)
                .member(findMember)
                .build()
            )
            .toList();
    }
}
