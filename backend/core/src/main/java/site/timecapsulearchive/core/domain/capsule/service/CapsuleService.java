package site.timecapsulearchive.core.domain.capsule.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CoordinateRangeDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.NearbyARCapsuleResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.NearbyCapsuleResponse;
import site.timecapsulearchive.core.domain.capsule.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.repository.CapsuleQueryRepository;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CapsuleService {

    private final CapsuleQueryRepository capsuleQueryRepository;
    private final GeoTransformManager geoTransformManager;
    private final CapsuleMapper capsuleMapper;

    /**
     * 현재 위치에서 범위 내에 있는 AR 캡슐을 조회한다.
     *
     * @param memberId    범위 내의 캡슙을 조회할 멤버 id
     * @param dto         범위 내의 캡슐 조회 요청 dto
     * @param capsuleType 캡슐의 타입
     * @return NearbyARCapsuleResponse 현재 위치 {@code dto.latitude()}, {@code dto.longitude()}에서 반경
     * {@code dto.distance()} 안에 캡슐 목록을 조회한다. 응답 좌표는 SRID 4326이다.
     */
    public NearbyARCapsuleResponse findARCapsuleByCurrentLocationAndCapsuleType(
        final Long memberId,
        final CoordinateRangeDto dto,
        final CapsuleType capsuleType
    ) {
        final Point point = geoTransformManager.changePoint4326To3857(dto.latitude(),
            dto.longitude());

        final Polygon mbr = geoTransformManager.getDistanceMBROf3857(point, dto.distance());

        return NearbyARCapsuleResponse.from(
            capsuleQueryRepository.findARCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
                    memberId, mbr, capsuleType)
                .stream()
                .map(capsuleMapper::nearByARCapsuleSummaryDtoToResponse)
                .toList()
        );
    }

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
                .map(NearbyCapsuleSummaryDto::toResponse)
                .toList()
        );
    }
}
