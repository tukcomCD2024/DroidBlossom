package site.timecapsulearchive.core.global.security.oauth.service;

import java.util.Collections;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.member.data.mapper.MemberMapper;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.global.security.oauth.dto.CustomOAuth2User;
import site.timecapsulearchive.core.global.security.oauth.dto.OAuthAttributes;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        SocialType socialType = SocialType.getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes extractAttributes = OAuthAttributes.of(
            socialType,
            userNameAttributeName,
            attributes
        );

        Member createMember = getMember(extractAttributes, socialType);

        return new CustomOAuth2User(
            Collections.emptyList(),
            attributes,
            userNameAttributeName,
            createMember.getEmail(),
            createMember.getIsVerified(),
            createMember.getId()
        );
    }


    private Member getMember(OAuthAttributes attributes, SocialType socialType) {
        return memberRepository.findMemberByAuthIdAndSocialType(
                attributes.getAuthId(),
                socialType
            )
            .orElseGet(() -> saveMember(socialType, attributes));
    }

    private Member saveMember(SocialType socialType, OAuthAttributes attributes) {
        Member createMember = memberMapper.OAuthToEntity(
            attributes.getAuthId(),
            socialType,
            attributes.getOauth2UserInfo()
        );
        return memberRepository.save(createMember);
    }
}
