package site.timecapsulearchive.core.infra.s3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.infra.s3.config.S3Properties;

@Component
@RequiredArgsConstructor
public class S3UrlGenerator {

    private final S3Properties s3Properties;

    public String generateS3UrlFormat(
        Long memberId,
        String directory,
        String fileName
    ) {
        return "https://"
            + s3Properties.bucket()
            + ".s3."
            + s3Properties.region()
            + ".amazonaws.com/"
            + generateFileName(memberId, directory, fileName);
    }

    public String generateFileName(
        Long memberId,
        String directory,
        String fileName
    ) {
        return directory + "/" + memberId + "/" + fileName;
    }
}
