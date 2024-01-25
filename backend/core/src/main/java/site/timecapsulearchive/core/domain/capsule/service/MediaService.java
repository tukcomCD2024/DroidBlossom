package site.timecapsulearchive.core.domain.capsule.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.FileMetaData;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.Image;
import site.timecapsulearchive.core.domain.capsule.entity.Video;
import site.timecapsulearchive.core.domain.capsule.repository.ImageRepository;
import site.timecapsulearchive.core.domain.capsule.repository.VideoRepository;
import site.timecapsulearchive.core.infra.s3.service.S3Service;

@Service
@RequiredArgsConstructor
public class MediaService {

    private static final String IMAGE_TYPE = "image/jpeg";
    private static final String VIDEO_TYPE = "video/mp4";

    private final S3Service s3Service;
    private final ImageRepository imageRepository;
    private final VideoRepository videoRepository;

    public void saveMediaData(Capsule capsule, String directory, Long memberId,
        List<FileMetaData> fileMetaDataList) {
        for (String imageUrl : getImageUrls(directory, memberId, fileMetaDataList)) {
            Image newImage = Image.builder()
                .imageUrl(imageUrl)
                .capsule(capsule)
                .build();

            imageRepository.save(newImage);
        }

        for (String videoUrl : getVideoUrls(directory, memberId, fileMetaDataList)) {
            Video video = Video.builder()
                .videoUrl(videoUrl)
                .capsule(capsule)
                .build();

            videoRepository.save(video);
        }
    }

    private List<String> getImageUrls(String directory, Long memberId,
        List<FileMetaData> fileMetaDataList) {
        return fileMetaDataList.stream()
            .filter(fileMetaData -> fileMetaData.extension().equals(IMAGE_TYPE))
            .map(fileMetaData -> s3Service.geS3UrlFormat(memberId, directory,
                fileMetaData.fileName()))
            .toList();
    }

    private List<String> getVideoUrls(String directory, Long memberId,
        List<FileMetaData> fileMetaDataList) {
        return fileMetaDataList.stream()
            .filter(fileMetaData -> fileMetaData.extension().equals(VIDEO_TYPE))
            .map(fileMetaData -> s3Service.geS3UrlFormat(memberId, directory,
                fileMetaData.fileName()))
            .toList();
    }
}
