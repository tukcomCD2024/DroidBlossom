package site.timecapsulearchive.core.domain.auth.service;

import java.util.Collections;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.auth.dto.oauth.CustomOAuth2User;
import site.timecapsulearchive.core.domain.auth.dto.oauth.OAuthAttributes;
import site.timecapsulearchive.core.domain.auth.entity.SocialType;
import site.timecapsulearchive.core.domain.member.dto.mapper.MemberMapper;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;

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
            .getProviderDetails().getUserInfoEndpoint()
            .getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName,
            attributes);
        Member createMember = getMember(socialType, extractAttributes);

        return new CustomOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(createMember.getRole().getKey())),
            attributes,
            extractAttributes.getNameAttributeKey(),
            createMember.getEmail(),
            createMember.getRole());
    }


    private Member getMember(SocialType socialType, OAuthAttributes attributes) {
        Member findMember = memberRepository.findBySocialTypeAndEmail(socialType,
            attributes.getOauth2UserInfo().getEmail()).orElse(null);
        if (findMember == null) {
            return saveMember(socialType, attributes);
        }

        return findMember;
    }

    private Member saveMember(SocialType socialType, OAuthAttributes attributes) {
        Member createMember = memberMapper.OAuthToEntity(socialType,
            attributes.getOauth2UserInfo());
        return memberRepository.save(createMember);
    }
}
