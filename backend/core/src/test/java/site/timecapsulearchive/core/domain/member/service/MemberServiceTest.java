package site.timecapsulearchive.core.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import site.timecapsulearchive.core.domain.member.data.dto.VerifiedCheckDto;
import site.timecapsulearchive.core.domain.member.data.response.MemberStatusResponse;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.domain.member.exception.NotVerifiedMemberException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.domain.member.repository.MemberTemporaryRepository;
import site.timecapsulearchive.core.global.error.ErrorCode;

class MemberServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final MemberTemporaryRepository memberTemporaryRepository = mock(
        MemberTemporaryRepository.class);

    private final MemberService memberService = new MemberService(
        memberRepository,
        memberTemporaryRepository
    );

    @Test
    void 인증된_사용자는_전화번호_인증을_했다() {
        //given
        String authId = "testAuthId";
        SocialType socialType = SocialType.KAKAO;
        given(memberRepository.findIsVerifiedByAuthIdAndSocialType(authId, socialType)).willReturn(
            Boolean.TRUE);

        //when
        MemberStatusResponse memberStatusResponse = memberService.checkStatus(authId, socialType);

        //then
        assertThat(memberStatusResponse.isVerified()).isTrue();
    }

    @Test
    void 인증되지_않은_사용자는_전화번호_인증을_하지_않았다() {
        //given
        String authId = "testAuthId";
        SocialType socialType = SocialType.KAKAO;
        given(memberRepository.findIsVerifiedByAuthIdAndSocialType(authId, socialType)).willReturn(
            Boolean.FALSE);

        //when
        MemberStatusResponse memberStatusResponse = memberService.checkStatus(authId, socialType);

        //then
        assertThat(memberStatusResponse.isVerified()).isFalse();
    }

    @Test
    void 로그인을_할_때_인증되지_않은_사용자는_예외가_발생한다() {
        //given
        String authId = "testAuthId";
        SocialType socialType = SocialType.KAKAO;
        VerifiedCheckDto verifiedCheckDto = new VerifiedCheckDto(1L, false);
        given(memberRepository.findVerifiedCheckDtoByAuthIdAndSocialType(authId,
            socialType)).willReturn(Optional.of(verifiedCheckDto));

        //when
        assertThatThrownBy(() -> memberService.findVerifiedMemberIdByAuthIdAndSocialType(
            authId, socialType))
            .isInstanceOf(NotVerifiedMemberException.class)
            .hasMessageContaining(ErrorCode.LOGIN_ON_NOT_VERIFIED_ERROR.getMessage());
    }
}
