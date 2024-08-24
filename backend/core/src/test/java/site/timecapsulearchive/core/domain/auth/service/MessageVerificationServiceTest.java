package site.timecapsulearchive.core.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import site.timecapsulearchive.core.common.dependency.UnitTestDependency;
import site.timecapsulearchive.core.domain.auth.data.dto.VerificationMessageSendDto;
import site.timecapsulearchive.core.domain.auth.exception.CertificationNumberNotFoundException;
import site.timecapsulearchive.core.domain.auth.exception.CertificationNumberNotMatchException;
import site.timecapsulearchive.core.domain.auth.exception.PhoneDuplicationException;
import site.timecapsulearchive.core.domain.auth.repository.MessageAuthenticationCacheRepository;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.infra.sms.manager.SmsApiManager;

class MessageVerificationServiceTest {

    private static final Long MEMBER_ID = 1L;
    private static final String RECEIVER = "010-0000-0000";
    private static final String APP_HASH_KEY = "test_key";

    private final MessageAuthenticationCacheRepository messageAuthenticationCacheRepository = mock(
        MessageAuthenticationCacheRepository.class);
    private final SmsApiManager smsApiManager = UnitTestDependency.smsApiManager();
    private final MemberRepository memberRepository = mock(MemberRepository.class);

    private final MessageVerificationService messageVerificationService = new MessageVerificationService(
        messageAuthenticationCacheRepository,
        smsApiManager,
        UnitTestDependency.hashEncryptionManager(),
        memberRepository
    );

    @Test
    void 중복된_번호가_있으면_예외가_발생한다() {
        // given
        given(memberRepository.checkPhoneHashDuplication(any())).willReturn(true);

        // when
        // then
        assertThatThrownBy(
            () -> messageVerificationService.sendVerificationMessage(MEMBER_ID, RECEIVER,
                APP_HASH_KEY))
            .isInstanceOf(PhoneDuplicationException.class);
    }

    @Test
    void 인증번호를_전송하면_성공한다() {
        //given
        given(memberRepository.checkPhoneHashDuplication(any())).willReturn(false);

        //when
        VerificationMessageSendDto verificationMessageSendDto = messageVerificationService.sendVerificationMessage(
            MEMBER_ID, RECEIVER, APP_HASH_KEY);

        //then
        assertThat(verificationMessageSendDto).isNotNull();
    }

    @Test
    void 저장된_인증번호를_찾을_수_없는_경우_예외가_발생한다() {
        //given
        String certificationNumber = "1234";
        given(messageAuthenticationCacheRepository.findMessageAuthenticationCodeByMemberId(
            anyLong(), any()))
            .willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> messageVerificationService.validVerificationMessage(
            MEMBER_ID, certificationNumber, RECEIVER.getBytes(StandardCharsets.UTF_8)))
            .isInstanceOf(CertificationNumberNotFoundException.class);
    }

    @Test
    void 일치하지_않은_인증번호로_인증을_시도하면_예외가_발생한다() {
        //given
        String certificationNumber = "1234";
        given(messageAuthenticationCacheRepository.findMessageAuthenticationCodeByMemberId(
            anyLong(), any()))
            .willReturn(Optional.of("3456"));

        //when
        //then
        assertThatThrownBy(() -> messageVerificationService.validVerificationMessage(
            MEMBER_ID, certificationNumber, RECEIVER.getBytes(StandardCharsets.UTF_8)))
            .isInstanceOf(CertificationNumberNotMatchException.class);
    }

}