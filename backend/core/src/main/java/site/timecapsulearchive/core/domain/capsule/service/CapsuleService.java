package site.timecapsulearchive.core.domain.capsule.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CoordinateRangeDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.CapsuleOpenedResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.NearbyCapsuleResponse;
import site.timecapsulearchive.core.domain.capsule.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.mapper.ImageMapper;
import site.timecapsulearchive.core.domain.capsule.mapper.VideoMapper;
import site.timecapsulearchive.core.domain.capsule.repository.CapsuleQueryRepository;
import site.timecapsulearchive.core.domain.capsule.repository.CapsuleRepository;
import site.timecapsulearchive.core.domain.capsule.repository.ImageQueryRepository;
import site.timecapsulearchive.core.domain.capsule.repository.VideoQueryRepository;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto.CapsuleCreateRequestDto;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.capsuleskin.exception.CapsuleSkinNotFoundException;
import site.timecapsulearchive.core.domain.capsuleskin.repository.CapsuleSkinRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CapsuleService {

    private final CapsuleQueryRepository capsuleQueryRepository;
    private final CapsuleRepository capsuleRepository;
    private final MemberRepository memberRepository;
    private final CapsuleSkinRepository capsuleSkinRepository;
    private final ImageQueryRepository imageQueryRepository;
    private final VideoQueryRepository videoQueryRepository;
    private final GeoTransformManager geoTransformManager;
    private final ImageMapper imageMapper;
    private final VideoMapper videoMapper;
    private final CapsuleMapper capsuleMapper;

    /**
     * 현재 위치에서 범위 내에 있는 캡슐을 조회한다.
     *
     * @param memberId    범위 내의 캡슙을 조회할 멤버 id
     * @param dto         범위 내의 캡슐 조회 요청 dto
     * @param capsuleType 캡슐의 타입
     * @return NearbyCapsuleResponse 현재 위치 {@code dto.latitude()}, {@code dto.longitude()}에서 반경
     * {@code dto.distance()} 안에 캡슐 목록을 조회한다. 응답 좌표는 SRID 4326이다.
     */
    public NearbyCapsuleResponse findCapsuleByCurrentLocationAndCapsuleType(
        final Long memberId,
        final CoordinateRangeDto dto,
        final CapsuleType capsuleType
    ) {
        final Point point = geoTransformManager.changePoint4326To3857(dto.latitude(),
            dto.longitude());

        final Polygon mbr = geoTransformManager.getDistanceMBROf3857(point, dto.distance());

        return NearbyCapsuleResponse.from(
            capsuleQueryRepository.findCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
                    memberId, mbr, capsuleType)
                .stream()
                .map(capsuleMapper::nearByCapsuleSummaryDtoToResponse)
                .toList()
        );
    }


    @Transactional
    public CapsuleOpenedResponse updateCapsuleOpened(final Long memberId, final Long capsuleId) {
        final Capsule findCapsule = capsuleRepository.findCapsuleByMemberIdAndCapsuleId(
            memberId,
            capsuleId
        ).orElseThrow(CapsuleNotFondException::new);

        if (findCapsule.isNotCapsuleOpened()) {
            return CapsuleOpenedResponse.notOpened();
        }

        capsuleRepository.updateIsOpenedTrue(memberId, capsuleId);
        return CapsuleOpenedResponse.opened();
    }

    /**
     * 멤버 아이디와 캡슐 생성 포맷을 받아서 캡슐을 생성한다.
     *
     * @param memberId 캡슐을 생성할 멤버 아이디
     * @param dto      캡슐 생성 요청 포맷
     */
    @Transactional
    public void saveCapsule(
        final Long memberId,
        final CapsuleCreateRequestDto dto,
        final CapsuleType capsuleType
    ) {
        final Member foundMember = memberRepository.findMemberById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        final CapsuleSkin foundCapsuleSkin = capsuleSkinRepository
            .findCapsuleSkinById(dto.capsuleSkinId())
            .orElseThrow(CapsuleSkinNotFoundException::new);

        final Capsule capsule = capsuleMapper.requestDtoToEntity(dto, foundMember,
            foundCapsuleSkin, capsuleType);

        capsuleRepository.save(capsule);

        if (isNotEmpty(dto.imageNames())) {
            imageQueryRepository.bulkSave(
                imageMapper.toEntity(capsule, foundMember, dto.directory(), dto.imageNames())
            );
        }

        if (isNotEmpty(dto.videoNames())) {
            videoQueryRepository.bulkSave(
                videoMapper.toEntity(capsule, foundMember, dto.directory(), dto.videoNames())
            );
        }
    }

    private boolean isNotEmpty(List<String> fileNames) {
        return fileNames != null && !fileNames.isEmpty();
    }
}
