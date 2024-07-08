package site.timecapsulearchive.core.domain.auth.service;

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
import site.timecapsulearchive.core.common.dependency.UnitTestDependency;
import site.timecapsulearchive.core.common.fixture.domain.MemberTemporaryFixture;
import site.timecapsulearchive.core.domain.auth.data.dto.VerificationMessageSendDto;
import site.timecapsulearchive.core.domain.auth.exception.CertificationNumberNotFoundException;
import site.timecapsulearchive.core.domain.auth.exception.CertificationNumberNotMatchException;
import site.timecapsulearchive.core.domain.auth.repository.MessageAuthenticationCacheRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.MemberTemporary;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.domain.member.repository.MemberTemporaryRepository;
import site.timecapsulearchive.core.infra.sms.manager.SmsApiManager;

class MessageVerificationServiceTest {

    private static final Long MEMBER_ID = 1L;
    private static final String RECEIVER = "010-0000-0000";
    private static final String APP_HASH_KEY = "test_key";

    private final MessageAuthenticationCacheRepository messageAuthenticationCacheRepository = mock(
        MessageAuthenticationCacheRepository.class);
    private final SmsApiManager smsApiManager = UnitTestDependency.smsApiManager();

    private final MessageVerificationService messageVerificationService = new MessageVerificationService(
        messageAuthenticationCacheRepository,
        smsApiManager,
        UnitTestDependency.hashEncryptionManager()
    );

    @Test
    void 인증번호를_전송하면_성공한다() {
        //given
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