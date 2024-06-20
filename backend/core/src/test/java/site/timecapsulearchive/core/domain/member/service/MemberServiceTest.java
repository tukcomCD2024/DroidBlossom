package site.timecapsulearchive.core.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import site.timecapsulearchive.core.domain.member.data.dto.EmailVerifiedCheckDto;
import site.timecapsulearchive.core.domain.member.data.mapper.MemberMapper;
import site.timecapsulearchive.core.domain.member.exception.CredentialsNotMatchedException;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.exception.NotVerifiedMemberException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.domain.member.repository.MemberTemporaryRepository;

class MemberServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final MemberTemporaryRepository memberTemporaryRepository = mock(
        MemberTemporaryRepository.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final MemberMapper memberMapper = mock(MemberMapper.class);

    private final MemberService memberService = new MemberService(
        memberRepository,
        memberTemporaryRepository,
        passwordEncoder,
        memberMapper
    );

    @Test
    void 이메일과_패스워드로_존재하지_않는_회원_아이디_검색하면_예외가_발생한다() {
        //given
        String email = "test@google.com";
        String password = "test-password";
        given(memberRepository.findEmailVerifiedCheckDtoByEmail(anyString()))
            .willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(
            () -> memberService.findVerifiedEmailMemberIdBy(email, password))
            .isExactlyInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 이메일과_패스워드로_검증된_회원_아이디_검색하면_올바른_아이디가_반환된다() {
        //given
        String email = "test@google.com";
        String password = "test-password";
        given(memberRepository.findEmailVerifiedCheckDtoByEmail(anyString()))
            .willReturn(getVerifiedCheckDto(Boolean.TRUE, email, password));

        //when
        Long id = memberService.findVerifiedEmailMemberIdBy(
            email, password);

        //then
        assertThat(id).isNotNull();
    }

    private Optional<EmailVerifiedCheckDto> getVerifiedCheckDto(Boolean isVerified, String email,
        String password) {
        return Optional.of(
            new EmailVerifiedCheckDto(
                1L,
                isVerified,
                email,
                (password == null) ? null : passwordEncoder.encode(password)
            )
        );
    }

    @Test
    void 이메일과_패스워드로_검증되지_않은_회원_아이디_검색하면_예외가_발생한다() {
        //given
        String email = "test@google.com";
        String password = "test-password";
        given(memberRepository.findEmailVerifiedCheckDtoByEmail(anyString()))
            .willReturn(getVerifiedCheckDto(Boolean.FALSE, email, password));

        //when, then
        assertThatThrownBy(
            () -> memberService.findVerifiedEmailMemberIdBy(email, password))
            .isExactlyInstanceOf(NotVerifiedMemberException.class);
    }

    @Test
    void 올바르지_않은_이메일로_검증된_회원_아이디_검색하면_예외가_발생한다() {
        //given
        String email = "test@google.com";
        String password = "test-password";
        given(memberRepository.findEmailVerifiedCheckDtoByEmail(anyString()))
            .willReturn(getVerifiedCheckDto(Boolean.TRUE, email, password));

        //when, then
        assertThatThrownBy(
            () -> memberService.findVerifiedEmailMemberIdBy(email + "trash", password))
            .isExactlyInstanceOf(CredentialsNotMatchedException.class);
    }

    @Test
    void 올바르지_않은_비밀번호로_검증된_회원_아이디_검색하면_예외가_발생한다() {
        //given
        String email = "test@google.com";
        String password = "test-password";
        given(memberRepository.findEmailVerifiedCheckDtoByEmail(anyString()))
            .willReturn(getVerifiedCheckDto(Boolean.TRUE, email, password));

        //when, then
        assertThatThrownBy(
            () -> memberService.findVerifiedEmailMemberIdBy(email, password + "trash"))
            .isExactlyInstanceOf(CredentialsNotMatchedException.class);
    }

    @Test
    void 비밀번호가_없는_검증된_회원_아이디_검색하면_예외가_발생한다() {
        //given
        String email = "test@google.com";
        String password = "test-password";
        given(memberRepository.findEmailVerifiedCheckDtoByEmail(anyString()))
            .willReturn(getVerifiedCheckDto(Boolean.TRUE, email, null));

        //when, then
        assertThatThrownBy(
            () -> memberService.findVerifiedEmailMemberIdBy(email, password))
            .isExactlyInstanceOf(CredentialsNotMatchedException.class);
    }

    @Test
    void 올바르지_않은_이메일과_비밀번호로_검증된_회원_아이디_검색하면_예외가_발생한다() {
        //given
        String email = "test@google.com";
        String password = "test-password";
        given(memberRepository.findEmailVerifiedCheckDtoByEmail(anyString()))
            .willReturn(getVerifiedCheckDto(Boolean.TRUE, email, password));

        //when, then
        assertThatThrownBy(
            () -> memberService.findVerifiedEmailMemberIdBy(email + "password",
                password + "trash"))
            .isExactlyInstanceOf(CredentialsNotMatchedException.class);
    }
}
