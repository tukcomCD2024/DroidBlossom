package site.timecapsulearchive.core.infra.s3.data.mapper;

import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.infra.s3.data.request.S3PreSignedUrlRequest;
import site.timecapsulearchive.core.infra.s3.data.request.S3PreSignedUrlRequestDto;

@Component
public class S3ApiMapper {

    public S3PreSignedUrlRequestDto s3PreSignedUrlRequestToDto(
        final S3PreSignedUrlRequest request) {
        return S3PreSignedUrlRequestDto.forPut(
            request.directory(),
            request.imageNames(),
            request.videoNames()
        );
    }
}
