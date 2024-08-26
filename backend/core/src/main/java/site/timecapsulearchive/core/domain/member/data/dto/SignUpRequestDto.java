package site.timecapsulearchive.core.domain.member.data.dto;

import site.timecapsulearchive.core.domain.member.entity.MemberTemporary;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.global.util.nickname.MakeRandomNickNameUtil;

public record SignUpRequestDto(
    String authId,
    String email,
    String profileUrl,
    SocialType socialType
) {

    public MemberTemporary toMemberTemporary(final String tag, final byte[] email,
        final byte[] emailHash) {
        return MemberTemporary.builder()
            .authId(authId)
            .nickname(MakeRandomNickNameUtil.makeRandomNickName())
            .profileUrl(profileUrl)
            .socialType(socialType)
            .tag(tag)
            .email(email)
            .emailHash(emailHash)
            .build();
    }
}
