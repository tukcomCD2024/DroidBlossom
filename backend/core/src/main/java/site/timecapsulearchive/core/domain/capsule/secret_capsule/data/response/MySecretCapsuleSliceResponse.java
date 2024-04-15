package site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Slice;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto.MySecreteCapsuleDto;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@Schema(description = "내가 생성한 비밀 캡슐 목록 응답")
public record MySecretCapsuleSliceResponse(

    @Schema(description = "내 비밀 캡슐 리스트")
    List<MySecreteCapsuleResponse> capsules,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext
) {

    public static MySecretCapsuleSliceResponse createOf(
        final Slice<MySecreteCapsuleDto> dtos,
        final S3PreSignedUrlManager s3PreSignedUrlManager
    ) {
        final List<MySecreteCapsuleResponse> capsules = dtos.getContent().
            stream()
            .map(dto -> dto.toResponse(s3PreSignedUrlManager::getS3PreSignedUrlForGet))
            .toList();

        return new MySecretCapsuleSliceResponse(capsules, dtos.hasNext());
    }

}
