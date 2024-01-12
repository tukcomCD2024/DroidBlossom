package site.timecapsulearchive.core.domain.auth.dto.oauth.google;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import site.timecapsulearchive.core.domain.auth.dto.oauth.SocialOauthUri;

@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google")
public class GoogleOauthUri implements SocialOauthUri {

    private static final String BASE_URI = "https://accounts.google.com/o/oauth2/v2/auth/oauthchooseaccount";

    private String clientId;
    private String redirectUri;
    private String scope;

    @Override
    public String getOauthRedirectURL() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("response_type", "code");
        queryParams.add("client_id", clientId);
        queryParams.add("scope", scope);
        queryParams.add("redirect_uri", redirectUri);

        return UriComponentsBuilder
            .fromUriString(BASE_URI)
            .queryParams(queryParams)
            .toUriString();
    }
}
