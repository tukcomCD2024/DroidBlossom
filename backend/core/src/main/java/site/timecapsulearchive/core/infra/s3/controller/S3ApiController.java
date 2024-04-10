package site.timecapsulearchive.core.infra.s3.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;
import site.timecapsulearchive.core.infra.s3.data.dto.S3PreSignedUrlDto;
import site.timecapsulearchive.core.infra.s3.data.mapper.S3ApiMapper;
import site.timecapsulearchive.core.infra.s3.data.request.ImageS3PreSignedUrlRequest;
import site.timecapsulearchive.core.infra.s3.data.request.S3PreSignedUrlRequest;
import site.timecapsulearchive.core.infra.s3.data.response.S3PreSignedUrlResponse;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3ApiController implements S3Api {

    private final S3PreSignedUrlManager s3PreSignedUrlManager;
    private final S3ApiMapper mapper;

    @PostMapping(
        value = "/upload-url",
        consumes = {"application/json"},
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<S3PreSignedUrlResponse>> getS3PreSignedUrl(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody final S3PreSignedUrlRequest request
    ) {
        final S3PreSignedUrlDto dto = s3PreSignedUrlManager.getS3PreSignedUrlsForPut(
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

    @PostMapping(
        value = "/image/upload-url",
        consumes = {"application/json"},
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<String>> getImageS3PreSignedUrl(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody ImageS3PreSignedUrlRequest request
    ) {
        String imageS3PreSignedUrlsForPut = s3PreSignedUrlManager.getImageS3PreSignedUrlsForPut(
            memberId, request.directory(), request.imageName());

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                imageS3PreSignedUrlsForPut
            )
        );
    }
}
