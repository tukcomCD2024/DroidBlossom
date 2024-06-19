package site.timecapsulearchive.core.domain.capsule.generic_capsule.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.Image;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.image.ImageRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.infra.s3.data.dto.S3Directory;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public void bulkSave(final List<String> fileNames, final Capsule capsule, final Member member) {
        if (isNotEmpty(fileNames)) {
            List<String> fullFileNames = fileNames.stream()
                .map(fileName -> S3Directory.CAPSULE.generateFullPath(member.getId(), fileName))
                .toList();

            imageRepository.bulkSave(Image.createOf(fullFileNames, capsule, member));
        }
    }

    private boolean isNotEmpty(final List<String> fileNames) {
        return fileNames != null && !fileNames.isEmpty();
    }
}
