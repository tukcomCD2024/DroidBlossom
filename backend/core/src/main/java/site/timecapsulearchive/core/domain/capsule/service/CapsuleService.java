package site.timecapsulearchive.core.domain.capsule.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.dto.AddressData;
import site.timecapsulearchive.core.domain.capsule.dto.CoordinateRangeRequestDto;
import site.timecapsulearchive.core.domain.capsule.dto.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.dto.response.CapsuleOpenedResponse;
import site.timecapsulearchive.core.domain.capsule.dto.response.NearbyCapsuleResponse;
import site.timecapsulearchive.core.domain.capsule.entity.Address;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.repository.CapsuleQueryRepository;
import site.timecapsulearchive.core.domain.capsule.repository.CapsuleRepository;
import site.timecapsulearchive.core.global.geography.GeoTransformer;
import site.timecapsulearchive.core.infra.map.MapApiService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CapsuleService {

    private final MapApiService mapApiService;
    private final CapsuleQueryRepository capsuleQueryRepository;
    private final CapsuleRepository capsuleRepository;
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
    public NearbyCapsuleResponse findCapsuleByCurrentLocationAndCapsuleType(
        Long memberId,
        CoordinateRangeRequestDto dto,
        CapsuleType capsuleType
    ) {
        Point point = geoTransformer.changePoint4326To3857(dto.latitude(), dto.longitude());

        Polygon mbr = geoTransformer.getDistanceMBROf3857(point, dto.distance());

        return NearbyCapsuleResponse.from(
            capsuleQueryRepository.findCapsuleByCurrentLocationAndCapsuleType(memberId, mbr,
                    capsuleType)
                .stream()
                .map(capsuleMapper::capsuleSummaryDtoToResponse)
                .toList()
        );
    }

    /**
     * 좌표를 통해 주소를 반환한다.
     *
     * @param latitude  위도
     * @param longitude 경도
     * @return 위도와 경도 데이터로 카카오 맵 주소룰 번환하다.
     */
    public AddressData getFullAddressByCoordinate(double latitude, double longitude) {
        Address address = mapApiService.reverseGeoCoding(longitude, latitude);

        return capsuleMapper.addressEntityToData(address);
    }


    @Transactional
    public CapsuleOpenedResponse updateCapsuleOpened(Long memberId, Long capsuleId) {
        String notOpened = "NO";
        String opened = "OK";

        Capsule findCapsule = capsuleRepository.findCapsuleByMemberIdAndCapsuleId(
            memberId,
            capsuleId
        ).orElseThrow(CapsuleNotFondException::new);

        if (findCapsule.isNotCapsuleOpened()) {
            return CapsuleOpenedResponse.from(notOpened);
        }

        findCapsule.updateOpened();
        return CapsuleOpenedResponse.from(opened);
    }
}
