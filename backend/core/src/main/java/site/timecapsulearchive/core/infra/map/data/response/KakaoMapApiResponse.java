package site.timecapsulearchive.core.infra.map.data.response;

import java.util.List;
import site.timecapsulearchive.core.infra.map.data.dto.Document;

public record KakaoMapApiResponse(
    List<Document> documents
) {

}