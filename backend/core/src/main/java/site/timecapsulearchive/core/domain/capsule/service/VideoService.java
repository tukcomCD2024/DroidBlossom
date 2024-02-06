package site.timecapsulearchive.core.domain.capsule.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.capsule.dto.MediaSaveDto;
import site.timecapsulearchive.core.domain.capsule.entity.Video;
import site.timecapsulearchive.core.domain.capsule.repository.VideoQueryRepository;
import site.timecapsulearchive.core.infra.s3.service.S3UrlGenerator;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoQueryRepository videoQueryRepository;
    private final S3UrlGenerator s3UrlGenerator;

    public void saveVideo(MediaSaveDto dto) {
        List<Video> videos = dto.fileNames().stream()
            .map(fileName -> Video.builder()
                .videoUrl(s3UrlGenerator.generateFileName(
                    dto.member().getId(),
                    dto.directory(),
                    fileName)
                )
                .capsule(dto.capsule())
                .member(dto.member())
                .build()
            )
            .toList();

        videoQueryRepository.bulkSave(videos);
    }
}
