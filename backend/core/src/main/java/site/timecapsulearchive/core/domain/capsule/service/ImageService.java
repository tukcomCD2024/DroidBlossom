package site.timecapsulearchive.core.domain.capsule.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.capsule.dto.MediaSaveDto;
import site.timecapsulearchive.core.domain.capsule.entity.Image;
import site.timecapsulearchive.core.domain.capsule.repository.ImageQueryRepository;
import site.timecapsulearchive.core.infra.s3.service.S3UrlGenerator;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageQueryRepository imageQueryRepository;
    private final S3UrlGenerator s3UrlGenerator;

    public void saveImage(MediaSaveDto dto) {
        List<Image> images = dto.fileNames().stream()
            .map(fileName -> Image.builder()
                .imageUrl(s3UrlGenerator.generateS3UrlFormat(
                    dto.member().getId(),
                    dto.directory(),
                    fileName)
                )
                .capsule(dto.capsule())
                .member(dto.member())
                .build()
            )
            .toList();

        imageQueryRepository.bulkSave(images);
    }
}
