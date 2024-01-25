package site.timecapsulearchive.core.infra.kakao.dto;

import java.util.List;

public record KakaoMapApiResponse(
    List<Document> documents
) {

}