package site.timecapsulearchive.core.domain.capsule.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.Video;
import site.timecapsulearchive.core.domain.capsule.repository.VideoQueryRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.infra.s3.data.dto.S3Directory;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoQueryRepository videoQueryRepository;

    @Transactional
    public void bulkSave(final List<String> fileNames, final Capsule capsule, final Member member) {
        if (isNotEmpty(fileNames)) {
            List<String> fullFileNames = fileNames.stream()
                .map(fileName -> S3Directory.CAPSULE.generateFullPath(member.getId(), fileName))
                .toList();

            videoQueryRepository.bulkSave(Video.createOf(fullFileNames, capsule, member));
        }
    }

    private boolean isNotEmpty(final List<String> fileNames) {
        return fileNames != null && !fileNames.isEmpty();
    }
}
