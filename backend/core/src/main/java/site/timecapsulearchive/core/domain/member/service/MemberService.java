package site.timecapsulearchive.core.domain.member.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.member.data.dto.MemberDetailDto;
import site.timecapsulearchive.core.domain.member.data.dto.MemberStatusDto;
import site.timecapsulearchive.core.domain.member.data.dto.SignUpRequestDto;
import site.timecapsulearchive.core.domain.member.data.dto.UpdateMemberDataDto;
import site.timecapsulearchive.core.domain.member.data.dto.VerifiedCheckDto;
import site.timecapsulearchive.core.domain.member.data.response.MemberNotificationStatusResponse;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.MemberTemporary;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.domain.member.exception.AlreadyVerifiedException;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.exception.MemberTagDuplicatedException;
import site.timecapsulearchive.core.domain.member.exception.NotVerifiedMemberException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.domain.member.repository.MemberTemporaryRepository;
import site.timecapsulearchive.core.global.security.encryption.AESEncryptionManager;
import site.timecapsulearchive.core.global.security.encryption.HashEncryptionManager;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberTemporaryRepository memberTemporaryRepository;
    private final HashEncryptionManager hashEncryptionManager;
    private final AESEncryptionManager aesEncryptionManager;

    @Transactional
    public Long createMember(final SignUpRequestDto dto) {
        final String tag = NanoIdUtils.randomNanoId();
        final byte[] emailBytes = dto.email().getBytes(StandardCharsets.UTF_8);
        final MemberTemporary member = dto.toMemberTemporary(
            tag,
            aesEncryptionManager.encryptWithPrefixIV(emailBytes),
            hashEncryptionManager.encrypt(emailBytes)
        );

        final MemberTemporary savedMember = memberTemporaryRepository.save(member);

        return savedMember.getId();
    }

    /**
     * 사용자의 소셜 프로바이더 인증 아이디와 타입을 받아 사용자의 인증 상태를 반환한다.
     *
     * @param authId     사용자의 소셜 프로바이더 인증 id
     * @param socialType 사용자의 소셜 프로바이더 타입
     * @return 사용자의 인증 상태 {@code isExist, isVerified}
     */
    public MemberStatusDto checkStatus(
        final String authId,
        final SocialType socialType
    ) {
        return memberRepository.findIsVerifiedByAuthIdAndSocialType(authId, socialType);
    }

    /**
     * 인증 아이디와 소셜 프로바이더 타입을 받아 인증된 회원을 조회한다.
     *
     * @param authId     사용자의 소셜 프로바이더 인증 id
     * @param socialType 사용자의 소셜 프로바이더 타입
     * @return 인증된 사용자의 아이디
     * @throws NotVerifiedMemberException 인증되지 않은 사용자인 경우에 발생하는 예외
     */
    public Long findVerifiedSocialMemberIdBy(
        final String authId,
        final SocialType socialType
    ) throws NotVerifiedMemberException {
        final VerifiedCheckDto dto = memberRepository.findVerifiedCheckDtoByAuthIdAndSocialType(
                authId, socialType)
            .orElseThrow(MemberNotFoundException::new);

        if (!dto.isVerified()) {
            throw new NotVerifiedMemberException();
        }

        return dto.memberId();
    }

    /**
     * 인증 아이디와 소셜 프로바이더 타입을 받아 인증되지 않은 회원을 조회한다.
     *
     * @param authId     사용자의 소셜 프로바이더 인증 id
     * @param socialType 사용자의 소셜 프로바이더 타입
     * @return 인증되지 않은 사용자의 아이디
     * @throws AlreadyVerifiedException 이미 인증된 사용자인 경우 발생하는 예외
     */
    public Long findNotVerifiedMemberIdBy(
        final String authId,
        final SocialType socialType
    ) {
        final VerifiedCheckDto dto = memberRepository.findVerifiedCheckDtoByAuthIdAndSocialType(
                authId, socialType)
            .orElseThrow(MemberNotFoundException::new);

        if (dto.isVerified()) {
            throw new AlreadyVerifiedException();
        }

        return dto.memberId();
    }

    public MemberDetailDto findMemberDetailById(final Long memberId) {
        return memberRepository.findMemberDetailResponseDtoById(memberId)
            .orElseThrow(MemberNotFoundException::new);
    }

    @Transactional
    public void updateMemberFCMToken(final Long memberId, final String fcmToken) {
        int updatedColumn = memberRepository.updateMemberFCMToken(memberId, fcmToken);

        if (updatedColumn != 1) {
            throw new MemberNotFoundException();
        }
    }

    @Transactional
    public void updateMemberNotificationEnabled(
        final Long memberId,
        final Boolean notificationEnabled
    ) {
        int updatedColumn = memberRepository.updateMemberNotificationEnabled(memberId,
            notificationEnabled);

        if (updatedColumn != 1) {
            throw new MemberNotFoundException();
        }
    }

    public MemberNotificationStatusResponse checkNotificationStatus(final Long memberId) {
        final Boolean isAlarm = memberRepository.findIsAlarmByMemberId(memberId)
            .orElseThrow(MemberNotFoundException::new);

        return new MemberNotificationStatusResponse(isAlarm);
    }

    public Member findMemberById(final Long memberId) {
        return memberRepository.findMemberById(memberId)
            .orElseThrow(MemberNotFoundException::new);
    }

    @Transactional
    public void updateMemberData(
        final Long memberId,
        final UpdateMemberDataDto updateMemberDataDto
    ) {
        final Member member = memberRepository.findMemberById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        member.updateData(updateMemberDataDto.nickname(), updateMemberDataDto.tag());
        try {
            memberRepository.saveAndFlush(member);
        } catch (DataIntegrityViolationException e) {
            throw new MemberTagDuplicatedException();
        }
    }

    @Transactional
    public void updateMemberTagSearchAvailable(
        final Long memberId,
        final Boolean tagSearchAvailable
    ) {
        final int updatedCount = memberRepository.updateMemberTagSearchAvailable(memberId,
            tagSearchAvailable);

        if (updatedCount != 1) {
            throw new MemberNotFoundException();
        }
    }

    @Transactional
    public void updateMemberPhoneSearchAvailable(
        final Long memberId,
        final Boolean phoneSearchAvailable
    ) {
        final int updatedCount = memberRepository.updateMemberPhoneSearchAvailable(memberId,
            phoneSearchAvailable);

        if (updatedCount != 1) {
            throw new MemberNotFoundException();
        }
    }

    @Transactional
    public void delete(final Member member) {
        memberRepository.delete(member);
    }

    @Transactional
    public void declarationMember(final Long targetId) {
        Member member = memberRepository.findMemberById(targetId)
            .orElseThrow(MemberNotFoundException::new);

        member.upDeclarationCount();
    }

    @Transactional
    public Long updateVerifiedMember(final Long memberId, final byte[] phoneBytes) {
        final MemberTemporary memberTemporary = memberTemporaryRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        memberTemporaryRepository.delete(memberTemporary);

        final Member verifiedMember = memberTemporary.toMember(
            hashEncryptionManager.encrypt(phoneBytes),
            aesEncryptionManager.encryptWithPrefixIV(phoneBytes)
        );

        memberRepository.save(verifiedMember);

        return verifiedMember.getId();
    }

    @Transactional
    public void updateMemberPhone(
        final Long memberId,
        final byte[] phonePlain
    ) {
        byte[] phoneHash = hashEncryptionManager.encrypt(phonePlain);
        byte[] encryptedPhone = aesEncryptionManager.encryptWithPrefixIV(phonePlain);

        int updatedCount = memberRepository.updateMemberPhoneHashAndPhone(memberId, phoneHash,
            encryptedPhone);
        if (updatedCount != 1) {
            throw new MemberNotFoundException();
        }
    }
}
