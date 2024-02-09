package site.timecapsulearchive.core.infra.s3.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.infra.s3.config.S3Properties;

@Component
@RequiredArgsConstructor
public class S3UrlGenerator {

    private final S3Properties s3Properties;

    public String generateFileName(
        final Long memberId,
        final String directory,
        final String fileName
    ) {
        return directory + "/" + memberId + "/" + fileName;
    }
}
