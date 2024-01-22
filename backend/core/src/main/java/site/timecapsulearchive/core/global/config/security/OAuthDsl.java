package site.timecapsulearchive.core.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class OAuthDsl extends AbstractHttpConfigurer<OAuthDsl, HttpSecurity> {

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> customOauth2UserService;
    private final AuthenticationSuccessHandler oAuth2LoginSuccessHandler;
    private final AuthenticationFailureHandler oauth2LoginFailureHandler;

    public static OAuthDsl oauthDsl(
        OAuth2UserService<OAuth2UserRequest, OAuth2User> customOauth2UserService,
        AuthenticationSuccessHandler oAuth2LoginSuccessHandler,
        AuthenticationFailureHandler oauth2LoginFailureHandler
    ) {
        return new OAuthDsl(
            customOauth2UserService,
            oAuth2LoginSuccessHandler,
            oauth2LoginFailureHandler
        );
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.oauth2Login(oauth2login -> oauth2login
            .userInfoEndpoint(
                userInfoEndpointConfig -> userInfoEndpointConfig.userService(
                    customOauth2UserService)
            )
            .authorizationEndpoint(authorization -> authorization
                .baseUri("/auth/login/")
            )
            .redirectionEndpoint(redirection -> redirection
                .baseUri("/auth/login/oauth2/code/*")
            )
            .successHandler(oAuth2LoginSuccessHandler)
            .failureHandler(oauth2LoginFailureHandler)
        );
    }

}
