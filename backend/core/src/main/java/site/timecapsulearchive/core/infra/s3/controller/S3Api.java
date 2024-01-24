package site.timecapsulearchive.core.infra.s3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.infra.s3.dto.request.S3PreSignedUrlRequest;
import site.timecapsulearchive.core.infra.s3.dto.response.S3PreSignedUrlResponse;

public interface S3Api {

    @Operation(
        summary = "s3 업로드 미리 서명된 주소 요청",
        description = "캡술 이미지/비디오 업로드할 수 있는 미리 서명된 주소를 응답한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"s3"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "s3업로드에 필요한 주소 반환"
        )
    })
    @PostMapping(
        value = "/upload-url",
        consumes = {"application/json"},
        produces = {"application/json"}
    )
    ResponseEntity<ApiSpec<S3PreSignedUrlResponse>> getS3PreSignedUrl(
        Long memberId,
        S3PreSignedUrlRequest request
    );
}
