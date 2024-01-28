package site.timecapsulearchive.core.domain.capsule.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.capsule.dto.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.SecretCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.SecreteCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.mapper.SecretCapsuleCreateRequestDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.response.SecretCapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.response.SecretCapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsule.entity.Address;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.repository.CapsuleQueryRepository;
import site.timecapsulearchive.core.domain.capsule.repository.CapsuleRepository;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.capsuleskin.exception.CapsuleSkinNotFoundException;
import site.timecapsulearchive.core.domain.capsuleskin.repository.CapsuleSkinRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.service.MemberService;
import site.timecapsulearchive.core.global.geography.GeoTransformer;
import site.timecapsulearchive.core.infra.map.MapApiService;

@Service
@RequiredArgsConstructor
public class SecreteCapsuleService {

    private final CapsuleQueryRepository capsuleQueryRepository;
    private final CapsuleRepository capsuleRepository;
    private final CapsuleSkinRepository capsuleSkinRepository;

    private final MemberService memberService;
    private final MediaService mediaService;
    private final MapApiService mapApiService;

    private final GeoTransformer geoTransformer;

    private final CapsuleMapper capsuleMapper;

    /**
     * 멤버 아이디와 캡슐 생성 포맷을 받아서 캡슐을 생성한다.
     *
     * @param memberId 캡슐을 생성할 멤버 아이디
     * @param dto      캡슐 생성 요청 포맷
     */
    public void createCapsule(Long memberId, SecretCapsuleCreateRequestDto dto) {
        Member findMember = memberService.findMemberByMemberId(memberId);

        Address address = mapApiService.reverseGeoCoding(
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

    /**
     * 멤버 아이디와 캡슐 아이디를 받아서 캡슐 요약 정보를 반환한다.
     *
     * @param memberId  멤버 아이디
     * @param capsuleId 캡슐 아이디
     * @return 캡슐 요약 정보
     */
    public SecretCapsuleSummaryResponse findSecretCapsuleSummaryById(
        Long memberId,
        Long capsuleId
    ) {
        SecretCapsuleSummaryDto dto = capsuleQueryRepository.findSecretCapsuleSummaryByMemberIdAndCapsuleId(
                memberId,
                capsuleId)
            .orElseThrow(CapsuleNotFondException::new);

        return capsuleMapper.secretCapsuleSummaryDtoToResponse(dto);
    }

    /**
     * 멤버 아이디와 캡슐 아이디를 받아서 캡슐 상세 정보를 반환한다.
     *
     * @param memberId 멤버 아이디
     * @param capsuleId 캡슐 아이디
     * @return 캡슐 상세 정보
     */
    public SecretCapsuleDetailResponse findSecretCapsuleDetailById(
        Long memberId,
        Long capsuleId
    ) {
        SecreteCapsuleDetailDto dto = capsuleQueryRepository.findSecreteCapsuleDetailByMemberIdAndCapsuleId(
                memberId,
                capsuleId)
            .orElseThrow(CapsuleNotFondException::new);

        return capsuleMapper.secretCapsuleDetailDtoToResponse(dto);
    }
}
