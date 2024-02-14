package site.timecapsulearchive.core.infra.s3.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3UrlGenerator {

    public String generateFileName(
        final Long memberId,
        final String directory,
        final String fileName
    ) {
        return directory + "/" + memberId + "/" + fileName;
    }
}
