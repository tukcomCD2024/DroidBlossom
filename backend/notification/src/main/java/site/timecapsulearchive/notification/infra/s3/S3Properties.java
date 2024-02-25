package site.timecapsulearchive.notification.infra.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "s3")
public record S3Properties(
    String accessKey,
    String secretKey,
    String bucket,
    String region
) {

}

