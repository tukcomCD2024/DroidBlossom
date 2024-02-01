package site.timecapsulearchive.core.domain.capsuleskin.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.capsuleskin.dto.reqeust.CapsuleSkinCreateRequest;
import site.timecapsulearchive.core.domain.capsuleskin.dto.response.CapsuleSkinSearchPageResponse;
import site.timecapsulearchive.core.domain.capsuleskin.dto.response.CapsuleSkinSummaryResponse;
import site.timecapsulearchive.core.domain.capsuleskin.dto.response.CapsuleSkinsPageResponse;

@RestController
@RequestMapping("/capsule-skins")
@RequiredArgsConstructor
public class CapsuleSkinApiController implements CapsuleSkinApi {

    @Override
    public ResponseEntity<CapsuleSkinSummaryResponse> createCapsuleSkin(
        @ModelAttribute CapsuleSkinCreateRequest request
    ) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteCapsuleSkin(Long capsuleSkinId) {
        return null;
    }

    @Override
    public ResponseEntity<CapsuleSkinSearchPageResponse> findCapsuleByName(
        @RequestParam(value = "capsule_skin_name") Long capsuleSkinName,
        @RequestParam(value = "size") Long size,
        @RequestParam(value = "capsule_skin_id") Long capsuleSkinId
    ) {
        return null;
    }

    @Override
    public ResponseEntity<CapsuleSkinsPageResponse> findCapsuleSkins(
        @RequestParam(value = "size") Long size,
        @RequestParam(value = "capsule_skin_id") Long capsuleSkinId
    ) {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateCapsuleSkin(Long capsuleSkinId) {
        return null;
    }
}
