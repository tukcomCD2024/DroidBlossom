package site.timecapsulearchive.core.infra.s3.dto.mapper;

import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.infra.s3.dto.request.S3PreSignedUrlRequest;
import site.timecapsulearchive.core.infra.s3.dto.request.S3PreSignedUrlRequestDto;

@Component
public class S3ApiMapper {

    public S3PreSignedUrlRequestDto s3PreSignedUrlRequestToDto(S3PreSignedUrlRequest request) {
        return S3PreSignedUrlRequestDto.from(
            request.directory(),
            request.imageUrls(),
            request.videoUrls()
        );
    }
}
