package site.timecapsulearchive.core.infra.map.dto;

import java.util.List;

public record KakaoMapApiResponse(
    List<Document> documents
) {

}