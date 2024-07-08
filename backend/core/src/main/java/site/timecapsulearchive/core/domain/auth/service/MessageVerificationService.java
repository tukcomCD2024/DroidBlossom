package site.timecapsulearchive.core.domain.auth.service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.auth.data.dto.VerificationMessageSendDto;
import site.timecapsulearchive.core.domain.auth.exception.CertificationNumberNotFoundException;
import site.timecapsulearchive.core.domain.auth.exception.CertificationNumberNotMatchException;
import site.timecapsulearchive.core.domain.auth.repository.MessageAuthenticationCacheRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.MemberTemporary;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.exception.MemberTagDuplicatedException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.domain.member.repository.MemberTemporaryRepository;
import site.timecapsulearchive.core.global.security.encryption.AESEncryptionManager;
import site.timecapsulearchive.core.global.security.encryption.HashEncryptionManager;
import site.timecapsulearchive.core.infra.sms.data.response.SmsApiResponse;
import site.timecapsulearchive.core.infra.sms.manager.SmsApiManager;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MessageVerificationService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int MIN = 1000;
    private static final int MAX = 10000;

    private final MessageAuthenticationCacheRepository messageAuthenticationCacheRepository;
    private final SmsApiManager smsApiManager;
    private final MemberRepository memberRepository;
    private final MemberTemporaryRepository memberTemporaryRepository;

    private final AESEncryptionManager aesEncryptionManager;
    private final HashEncryptionManager hashEncryptionManager;

    /**
     * 사용자 아이디와 수신자 핸드폰을 받아서 인증번호를 발송한다.
     *
     * @param memberId   사용자 아이디
     * @param receiver   수신자 핸드폰 번호
     * @param appHashKey 앱의 해시 키(메시지 자동 파싱)
     */
    public VerificationMessageSendDto sendVerificationMessage(
        final Long memberId,
        final String receiver,
        final String appHashKey
    ) {
        final String code = generateRandomCode();
        final String message = generateMessage(code, appHashKey);
        final SmsApiResponse apiResponse = smsApiManager.sendMessage(receiver, message);

        final byte[] plain = receiver.getBytes(StandardCharsets.UTF_8);
        byte[] encrypt = hashEncryptionManager.encrypt(plain);

        messageAuthenticationCacheRepository.save(memberId, encrypt, code);

        return VerificationMessageSendDto.success(apiResponse.resultCode(),
            apiResponse.message());
    }

    private String generateRandomCode() {
        return String.valueOf(
            SECURE_RANDOM.nextInt(MIN, MAX)
        );
    }

    private String generateMessage(final String code, final String appHashKey) {
        return "[ARchive]"
            + "본인확인 인증번호는 ["
            + code
            + "]입니다."
            + "타인 노출 금지";
    }

    public Long validVerificationMessage(
        final Long memberId,
        final String certificationNumber,
        final String receiver
    ) {
        final byte[] plain = receiver.getBytes(StandardCharsets.UTF_8);
        byte[] encrypt = hashEncryptionManager.encrypt(plain);

        final String findCertificationNumber = messageAuthenticationCacheRepository
            .findMessageAuthenticationCodeByMemberId(memberId, encrypt)
            .orElseThrow(CertificationNumberNotFoundException::new);

        if (isNotMatch(certificationNumber, findCertificationNumber)) {
            throw new CertificationNumberNotMatchException();
        }

        return updateToVerifiedMember(memberId, plain);
    }

    private boolean isNotMatch(final String certificationNumber,
        final String findCertificationNumber) {
        return !certificationNumber.equals(findCertificationNumber);
    }

    private Long updateToVerifiedMember(final Long memberId, final byte[] plain) {
        final MemberTemporary memberTemporary = memberTemporaryRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        memberTemporaryRepository.delete(memberTemporary);

        boolean isDuplicateTag = memberRepository.checkTagDuplication(memberTemporary.getTag());
        if (isDuplicateTag) {
            log.warn("member tag duplicate - email:{}, tag:{}", memberTemporary.getEmail(),
                memberTemporary.getTag());
            memberTemporary.updateTagLowerCaseSocialType();
            log.warn("member tag update - tag: {}", memberTemporary.getTag());
        }

        final Member verifiedMember = memberTemporary.toMember(hashEncryptionManager.encrypt(plain),
            aesEncryptionManager.encryptWithPrefixIV(plain));

        try {
            memberRepository.saveAndFlush(verifiedMember);
        } catch (DataIntegrityViolationException e) {
            throw new MemberTagDuplicatedException();
        }

        return verifiedMember.getId();
    }
}
