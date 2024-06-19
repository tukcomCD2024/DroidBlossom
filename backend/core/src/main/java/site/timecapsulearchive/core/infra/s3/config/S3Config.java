package site.timecapsulearchive.core.infra.s3.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final S3Properties s3Properties;

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner
            .builder()
            .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials()))
            .region(Region.of(s3Properties.region()))
            .build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials()))
            .region(Region.of(s3Properties.region()))
            .build();
    }

    private AwsBasicCredentials awsBasicCredentials() {
        return AwsBasicCredentials.create(s3Properties.accessKey(), s3Properties.secretKey());
    }

    public String getBucketName() {
        return s3Properties.bucket();
    }

    public String getTemporaryBucketName() {
        return s3Properties.temporaryBucket();
    }
}