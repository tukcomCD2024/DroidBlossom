package site.timecapsulearchive.core.domain.auth.service;

import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.auth.dto.response.TokenResponse;
import site.timecapsulearchive.core.domain.auth.dto.response.VerificationMessageSendResponse;
import site.timecapsulearchive.core.domain.auth.exception.NotFoundCertificationNumberException;
import site.timecapsulearchive.core.domain.auth.exception.NotMatchCertificationNumberException;
import site.timecapsulearchive.core.domain.auth.repository.MessageAuthenticationCacheRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.service.MemberService;
import site.timecapsulearchive.core.infra.sms.SmsApiService;
import site.timecapsulearchive.core.infra.sms.dto.SmsApiResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageVerificationService {

    private static final int MIN = 1000;
    private static final int MAX = 10000;

    private final MessageAuthenticationCacheRepository messageAuthenticationCacheRepository;
    private final SmsApiService smsApiService;
    private final MemberService memberService;
    private final TokenService tokenService;


    /**
     * 사용자 아이디와 수신자 핸드폰을 받아서 인증번호를 발송한다.
     *
     * @param memberId   사용자 아이디
     * @param receiver   수신자 핸드폰 번호
     * @param appHashKey 앱의 해시 키(메시지 자동 파싱)
     */
    public VerificationMessageSendResponse sendVerificationMessage(
        final Long memberId,
        final String receiver,
        final String appHashKey
    ) {
        final String code = generateRandomCode();

        final String message = generateMessage(code, appHashKey);

        final SmsApiResponse apiResponse = smsApiService.sendMessage(receiver, message);

        messageAuthenticationCacheRepository.save(memberId, code);

        return VerificationMessageSendResponse.success(apiResponse.resultCode(),
            apiResponse.message());
    }

    private String generateRandomCode() {
        return String.valueOf(
            ThreadLocalRandom.current()
                .nextInt(MIN, MAX)
        );
    }

    private String generateMessage(final String code, final String appHashKey) {
        return "<#>[ARchive]"
            + "본인확인 인증번호는 ["
            + code
            + "]입니다."
            + appHashKey;
    }

    public TokenResponse getRandomNickname(
        final Long memberId,
        final String certificationNumber,
        final String receiver
    ) {
        String findCertificationNumber = messageAuthenticationCacheRepository.get(memberId)
            .orElseThrow(NotFoundCertificationNumberException::new);

        if (isNotMatch(certificationNumber, findCertificationNumber)) {
            throw new NotMatchCertificationNumberException();
        }

        Member findMember = memberService.findMemberByMemberId(memberId);
        findMember.updateVerification();
        findMember.updatePhoneNumber(receiver);
        findMember.updateNickName();

        return tokenService.createNewToken(memberId);
    }

    private boolean isNotMatch(String certificationNumber, String findCertificationNumber) {
        return !certificationNumber.equals(findCertificationNumber);
    }
}
