package site.timecapsulearchive.core.infra.s3.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;
import site.timecapsulearchive.core.infra.s3.dto.S3PreSignedUrlDto;
import site.timecapsulearchive.core.infra.s3.dto.mapper.S3ApiMapper;
import site.timecapsulearchive.core.infra.s3.dto.request.S3PreSignedUrlRequest;
import site.timecapsulearchive.core.infra.s3.dto.response.S3PreSignedUrlResponse;
import site.timecapsulearchive.core.infra.s3.service.S3PreSignedUrlManager;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class S3ApiController implements S3Api {

    private final S3PreSignedUrlManager s3Service;
    private final S3ApiMapper mapper;

    @Override
    public ResponseEntity<ApiSpec<S3PreSignedUrlResponse>> getS3PreSignedUrl(
        @AuthenticationPrincipal final Long memberId,
        @RequestBody @Valid final S3PreSignedUrlRequest request
    ) {
        S3PreSignedUrlDto dto = s3Service.getS3PreSignedUrlsForPut(
            memberId,
            mapper.s3PreSignedUrlRequestToDto(request)
        );

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                S3PreSignedUrlResponse.from(dto.preSignedImageUrls(), dto.preSignedVideoUrls())
            )
        );
    }
}
