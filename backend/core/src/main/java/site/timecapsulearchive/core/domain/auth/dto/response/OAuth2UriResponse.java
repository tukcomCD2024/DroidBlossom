package site.timecapsulearchive.core.domain.auth.dto.response;

public record OAuth2UriResponse(
    String url
) {

    public static OAuth2UriResponse from(String url) {
        return new OAuth2UriResponse(url);
    }
}
