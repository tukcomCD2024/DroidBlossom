package site.timecapsulearchive.core.infra.map;

import site.timecapsulearchive.core.domain.capsule.entity.Address;

public interface MapApiService {

    /**
     * 경도와 위도를 받아서 주소를 반환한다.
     *
     * @param longitude 경도
     * @param latitude  위도
     * @return 위도와 경도로부터 반환된 주소
     */
    Address reverseGeoCoding(Double longitude, Double latitude);
}
