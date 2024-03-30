package site.timecapsulearchive.core.domain.capsule.public_capsule.api;

import org.springframework.http.ResponseEntity;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.reqeust.PublicCapsuleUpdateRequest;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.response.PublicCapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.response.PublicCapsulePageResponse;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.response.PublicCapsuleSummaryResponse;

public class PublicCapsuleApiController implements PublicCapsuleApi {

    @Override
    public ResponseEntity<PublicCapsuleDetailResponse> findPublicCapsuleById(Long capsuleId) {
        return null;
    }

    @Override
    public ResponseEntity<PublicCapsulePageResponse> getPublicCapsules(Long size, Long capsuleId) {
        return null;
    }

    @Override
    public ResponseEntity<PublicCapsuleSummaryResponse> updatePublicCapsuleById(Long capsuleId,
        PublicCapsuleUpdateRequest request) {
        return null;
    }
}
