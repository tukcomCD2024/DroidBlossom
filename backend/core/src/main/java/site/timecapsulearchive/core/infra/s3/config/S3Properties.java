package site.timecapsulearchive.core.infra.s3.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "s3")
public record S3Properties(
    String accessKey,
    String secretKey,
    String bucket,
    String temporaryBucket,
    String region
) {

}

