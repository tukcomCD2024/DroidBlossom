package site.timecapsulearchive.core.infra.s3.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;
import site.timecapsulearchive.core.infra.s3.data.request.ImageS3PreSignedUrlRequest;
import site.timecapsulearchive.core.infra.s3.data.request.S3PreSignedUrlRequest;
import site.timecapsulearchive.core.infra.s3.data.response.S3PreSignedUrlResponse;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3ApiController implements S3Api {

    private final S3PreSignedUrlManager s3PreSignedUrlManager;

    @PostMapping(
        value = "/upload-urls",
        consumes = {"application/json"},
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<S3PreSignedUrlResponse>> getS3PreSignedUrl(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody final S3PreSignedUrlRequest request
    ) {
        final List<String> preSignedImagePaths = s3PreSignedUrlManager.preSignMultipleImagesForPut(
            request.directory(),
            memberId,
            request.imageNames()
        );

        final List<String> preSignedVideoPaths = s3PreSignedUrlManager.preSignMultipleVideosForPut(
            request.directory(),
            memberId,
            request.videoNames()
        );

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                S3PreSignedUrlResponse.from(preSignedImagePaths, preSignedVideoPaths)
            )
        );
    }

    @PostMapping(
        value = "/upload-url/image",
        consumes = {"application/json"},
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<String>> getImageS3PreSignedUrl(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody final ImageS3PreSignedUrlRequest request
    ) {
        String preSignedImagePath = s3PreSignedUrlManager.preSignSingleImageForPut(
            request.s3Directory(),
            memberId,
            request.imageName()
        );

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                preSignedImagePath
            )
        );
    }
}
