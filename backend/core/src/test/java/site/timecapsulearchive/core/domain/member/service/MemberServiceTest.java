package site.timecapsulearchive.core.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import site.timecapsulearchive.core.common.fixture.domain.MemberTemporaryFixture;
import site.timecapsulearchive.core.domain.member.data.dto.MemberStatusDto;
import site.timecapsulearchive.core.domain.member.data.dto.VerifiedCheckDto;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.MemberTemporary;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.domain.member.exception.NotVerifiedMemberException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.domain.member.repository.MemberTemporaryRepository;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.security.encryption.AESEncryptionManager;
import site.timecapsulearchive.core.global.security.encryption.HashEncryptionManager;

class MemberServiceTest {

    private static final Long MEMBER_ID = 1L;
    private static final String RECEIVER = "010-0000-0000";

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final MemberTemporaryRepository memberTemporaryRepository = mock(
        MemberTemporaryRepository.class);
    private final HashEncryptionManager hashEncryptionManager = mock(HashEncryptionManager.class);
    private final AESEncryptionManager aesEncryptionManager = mock(AESEncryptionManager.class);

    private final MemberService memberService = new MemberService(
        memberRepository,
        memberTemporaryRepository,
        hashEncryptionManager,
        aesEncryptionManager
    );

    @Test
    void 인증된_사용자는_전화번호_인증을_했다() {
        //given
        String authId = "testAuthId";
        SocialType socialType = SocialType.KAKAO;
        given(memberRepository.findIsVerifiedByAuthIdAndSocialType(authId, socialType)).willReturn(
            new MemberStatusDto(true, true, true));

        //when
        MemberStatusDto memberStatusResponse = memberService.checkStatus(authId, socialType);

        //then
        assertThat(memberStatusResponse.isVerified()).isTrue();
    }

    @Test
    void 인증되지_않은_사용자는_전화번호_인증을_하지_않았다() {
        //given
        String authId = "testAuthId";
        SocialType socialType = SocialType.KAKAO;
        given(memberRepository.findIsVerifiedByAuthIdAndSocialType(authId, socialType)).willReturn(
            new MemberStatusDto(false, false, false));

        //when
        MemberStatusDto memberStatusResponse = memberService.checkStatus(authId, socialType);

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
        assertThatThrownBy(() -> memberService.findVerifiedSocialMemberIdBy(
            authId, socialType))
            .isInstanceOf(NotVerifiedMemberException.class)
            .hasMessageContaining(ErrorCode.LOGIN_ON_NOT_VERIFIED_ERROR.getMessage());
    }


    @Test
    void 인증번호가_일치하면_사용자를_저장한다() {
        //given
        given(memberTemporaryRepository.findById(anyLong()))
            .willReturn(Optional.of(MemberTemporaryFixture.memberTemporary(MEMBER_ID)));
        given(memberRepository.checkTagDuplication(any())).willReturn(false);

        //when
        memberService.updateVerifiedMember(MEMBER_ID, RECEIVER.getBytes(StandardCharsets.UTF_8));

        //then
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void 태그가_중복되면_태그를_교체한다() {
        //given
        MemberTemporary memberTemporary = MemberTemporaryFixture.memberTemporary(MEMBER_ID);
        String originTag = memberTemporary.getTag();
        given(memberTemporaryRepository.findById(anyLong()))
            .willReturn(Optional.of(memberTemporary));
        given(memberRepository.checkTagDuplication(any())).willReturn(true);

        //when
        memberService.updateVerifiedMember(MEMBER_ID, RECEIVER.getBytes(StandardCharsets.UTF_8));

        //then
        assertThat(memberTemporary.getTag()).isNotEqualTo(originTag);
    }
}
