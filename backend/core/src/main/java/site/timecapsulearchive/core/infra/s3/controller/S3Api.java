package site.timecapsulearchive.core.infra.s3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.infra.s3.data.request.ImageS3PreSignedUrlRequest;
import site.timecapsulearchive.core.infra.s3.data.request.S3PreSignedUrlRequest;
import site.timecapsulearchive.core.infra.s3.data.response.S3PreSignedUrlResponse;

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
        ),
        @ApiResponse(
            responseCode = "400",
            description = """
                올바르지 않은 입력인 경우 발생
                
                1. 리스트 크기 제한을 넘는 경우 ex) imageNames.size() > 20 or videoNames.size() > 20
                2. 올바르지 않은 입력 포맷이나 형식인 경우 ex) png, mp4가 아닌 경우
                """
        )
    })
    ResponseEntity<ApiSpec<S3PreSignedUrlResponse>> getS3PreSignedUrl(
        Long memberId,
        S3PreSignedUrlRequest request
    );

    @Operation(
        summary = "단일 이미지 s3 업로드 미리 서명된 주소 요청",
        description = "그룹 이미지를 업로드할 수 있는 미리 서명된 주소를 응답한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"s3"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "s3업로드에 필요한 주소 반환"
        ),
        @ApiResponse(
            responseCode = "400",
            description = """
                올바르지 않은 입력인 경우 발생
                
                1. 올바르지 않은 입력 포맷이나 형식인 경우 ex) png, mp4가 아닌 경우
                """
        )
    })
    ResponseEntity<ApiSpec<String>> getImageS3PreSignedUrl(
        Long memberId,
        ImageS3PreSignedUrlRequest request
    );
}
