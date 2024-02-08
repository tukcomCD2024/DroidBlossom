package site.timecapsulearchive.core.infra.map.service;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import site.timecapsulearchive.core.domain.capsule.data.mapper.AddressMapper;
import site.timecapsulearchive.core.domain.capsule.entity.Address;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.infra.map.config.KakaoMapProperties;
import site.timecapsulearchive.core.infra.map.data.dto.Document;
import site.timecapsulearchive.core.infra.map.data.response.KakaoMapApiResponse;
import site.timecapsulearchive.core.infra.sms.exception.ExternalApiException;

@Service
@RequiredArgsConstructor
public class KakaoMapApiService implements MapApiService {

    private static final String URL = "https://dapi.kakao.com";
    private static final String PATH = "/v2/local/geo/coord2address.json";
    private static final String LONGITUDE = "x";
    private static final String LATITUDE = "y";

    private final KakaoMapProperties kakaoMapProperties;
    private final RestTemplate restTemplate;
    private final AddressMapper addressMapper;

    public Address reverseGeoCoding(Double longitude, Double latitude) {
        URI uri = getKakaoMapApiUrl(longitude, latitude);

        HttpEntity<Void> entity = getHttpEntity();

        KakaoMapApiResponse response = restTemplate.exchange(
            uri,
            HttpMethod.GET,
            entity,
            KakaoMapApiResponse.class
        ).getBody();

        if (isError(response)) {
            throw new ExternalApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        Document document = response.documents().get(0);
        if (document.roadAddressDto() == null) {
            return addressMapper.addressDtoToAddress(document.addressDto());
        }

        return addressMapper.roadAddressToAddress(document.roadAddressDto());
    }

    private URI getKakaoMapApiUrl(Double longitude, Double latitude) {
        return UriComponentsBuilder.fromHttpUrl(URL)
            .path(PATH)
            .queryParam(LONGITUDE, longitude)
            .queryParam(LATITUDE, latitude)
            .build()
            .toUri();
    }

    private boolean isError(KakaoMapApiResponse response) {
        return response == null ||
            response.documents() == null;
    }

    private HttpEntity<Void> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, kakaoMapProperties.apiKey());

        return new HttpEntity<>(null, headers);
    }
}
