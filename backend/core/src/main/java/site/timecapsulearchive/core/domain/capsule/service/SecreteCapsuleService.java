package site.timecapsulearchive.core.domain.capsule.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.mapper.CapsuleCreateRequestDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.entity.Address;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.repository.CapsuleRepository;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.capsuleskin.exception.CapsuleSkinNotFoundException;
import site.timecapsulearchive.core.domain.capsuleskin.repository.CapsuleSkinRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.service.MemberService;
import site.timecapsulearchive.core.global.geography.GeoTransformer;
import site.timecapsulearchive.core.infra.kakao.service.KakaoMapApiService;

@Service
@RequiredArgsConstructor
public class SecreteCapsuleService {

    private final CapsuleRepository capsuleRepository;
    private final CapsuleSkinRepository capsuleSkinRepository;

    private final MemberService memberService;
    private final MediaService mediaService;
    private final KakaoMapApiService kakaoMapApiService;

    private final GeoTransformer geoTransformer;

    private final CapsuleMapper capsuleMapper;

    public void createCapsule(Long memberId, CapsuleCreateRequestDto dto) {
        Member findMember = memberService.findMemberByMemberId(memberId);

        Address address = kakaoMapApiService.reverseGeoCoding(
            dto.longitude(),
            dto.latitude()
        );

        CapsuleSkin capsuleSkin = capsuleSkinRepository
            .findById(dto.capsuleSkinId())
            .orElseThrow(CapsuleSkinNotFoundException::new);

        Point point = geoTransformer.changePoint4326To3857(dto.latitude(), dto.longitude());

        Capsule newCapsule = capsuleRepository.save(
            capsuleMapper.requestDtoToEntity(dto, point, address, findMember, capsuleSkin));

        mediaService.saveMediaData(newCapsule, dto.directory(), findMember.getId(),
            dto.fileNames());
    }
}
