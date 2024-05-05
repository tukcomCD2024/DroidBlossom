package site.timecapsulearchive.core.domain.member.data.mapper;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.member.data.dto.MemberNotificationDto;
import site.timecapsulearchive.core.domain.member.data.response.MemberNotificationResponse;
import site.timecapsulearchive.core.domain.member.data.response.MemberNotificationSliceResponse;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.global.security.oauth.dto.OAuth2UserInfo;
import site.timecapsulearchive.core.global.util.nickname.MakeRandomNickNameUtil;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@Component
@RequiredArgsConstructor
public class MemberMapper {

    private static final ZoneId ASIA_SEOUL = ZoneId.of("Asia/Seoul");
    private final S3PreSignedUrlManager s3PreSignedUrlManager;

    public Member OAuthToEntity(
        final String authId,
        final SocialType socialType,
        final OAuth2UserInfo oAuth2UserInfo
    ) {
        return Member.builder()
            .authId(authId)
            .nickname(MakeRandomNickNameUtil.makeRandomNickName())
            .email(oAuth2UserInfo.getEmail())
            .profileUrl(oAuth2UserInfo.getImageUrl())
            .socialType(socialType)
            .tag(NanoIdUtils.randomNanoId())
            .build();
    }

    public MemberNotificationSliceResponse notificationSliceToResponse(
        final List<MemberNotificationDto> content,
        final boolean hasNext
    ) {
        List<MemberNotificationResponse> responses = content.stream()
            .map(dto -> {
                    String imageUrl = "";
                    if (dto.imageUrl() != null) {
                        imageUrl = s3PreSignedUrlManager.getS3PreSignedUrlForGet(dto.imageUrl());
                    }

                    return MemberNotificationResponse.builder()
                        .title(dto.title())
                        .text(dto.text())
                        .createdAt(dto.createdAt().withZoneSameInstant(ASIA_SEOUL))
                        .imageUrl(imageUrl)
                        .categoryName(dto.categoryName())
                        .status(dto.status())
                        .build();
                }
            )
            .toList();

        return new MemberNotificationSliceResponse(responses, hasNext);
    }

    public Member createMemberWithEmail(String email, String password) {
        return Member.builder()
            .email(email)
            .password(password)
            .authId(String.valueOf(UUID.randomUUID()))
            .profileUrl("")
            .socialType(SocialType.EMAIL)
            .tag(NanoIdUtils.randomNanoId())
            .build();
    }
}