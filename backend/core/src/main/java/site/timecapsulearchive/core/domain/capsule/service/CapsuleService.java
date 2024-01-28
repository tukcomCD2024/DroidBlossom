package site.timecapsulearchive.core.domain.capsule.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.capsule.dto.CoordinateRangeRequestDto;
import site.timecapsulearchive.core.domain.capsule.dto.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.dto.response.NearbyCapsulePageResponse;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.repository.CapsuleQueryRepository;
import site.timecapsulearchive.core.global.geography.GeoTransformer;

@Service
@RequiredArgsConstructor
public class CapsuleService {

    private final CapsuleQueryRepository capsuleQueryRepository;
    private final GeoTransformer geoTransformer;
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
    public NearbyCapsulePageResponse findCapsuleByCurrentLocationAndCapsuleType(
        Long memberId,
        CoordinateRangeRequestDto dto,
        CapsuleType capsuleType
    ) {
        Point point = geoTransformer.changePoint4326To3857(dto.latitude(), dto.longitude());

        Polygon mbr = geoTransformer.getDistanceMBROf3857(point, dto.distance());

        return NearbyCapsulePageResponse.from(
            capsuleQueryRepository.findCapsuleByCurrentLocationAndCapsuleType(memberId, mbr,
                    capsuleType)
                .stream()
                .map(capsuleMapper::capsuleSummaryResponseToDto)
                .toList()
        );
    }
}
