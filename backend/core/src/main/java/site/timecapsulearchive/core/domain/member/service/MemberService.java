package site.timecapsulearchive.core.domain.member.service;

import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.member.data.dto.EmailVerifiedCheckDto;
import site.timecapsulearchive.core.domain.member.data.dto.MemberDetailDto;
import site.timecapsulearchive.core.domain.member.data.dto.MemberNotificationDto;
import site.timecapsulearchive.core.domain.member.data.dto.SignUpRequestDto;
import site.timecapsulearchive.core.domain.member.data.dto.VerifiedCheckDto;
import site.timecapsulearchive.core.domain.member.data.mapper.MemberMapper;
import site.timecapsulearchive.core.domain.member.data.response.CheckEmailDuplicationResponse;
import site.timecapsulearchive.core.domain.member.data.response.MemberNotificationSliceResponse;
import site.timecapsulearchive.core.domain.member.data.response.MemberNotificationStatusResponse;
import site.timecapsulearchive.core.domain.member.data.response.MemberStatusResponse;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.MemberTemporary;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.domain.member.exception.AlreadyVerifiedException;
import site.timecapsulearchive.core.domain.member.exception.CredentialsNotMatchedException;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.exception.NotVerifiedMemberException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.domain.member.repository.MemberTemporaryRepository;
import site.timecapsulearchive.core.global.util.TagGenerator;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberTemporaryRepository memberTemporaryRepository;
    private final PasswordEncoder passwordEncoder;

    private final MemberMapper memberMapper;

    @Transactional
    public Long createMember(final SignUpRequestDto dto) {
        final String tag = TagGenerator.generate(dto.email(), dto.socialType());
        final MemberTemporary member = dto.toMemberTemporary(tag);

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
    public MemberStatusResponse checkStatus(
        final String authId,
        final SocialType socialType
    ) {

        final Boolean isVerified = memberRepository.findIsVerifiedByAuthIdAndSocialType(
            authId,
            socialType
        );

        if (isVerified == null) {
            return MemberStatusResponse.empty();
        }

        return MemberStatusResponse.from(isVerified);
    }

    /**
     * 인증 아이디와 소셜 프로바이더 타입을 받아 인증된 회원을 조회한다.
     *
     * @param authId     사용자의 소셜 프로바이더 인증 id
     * @param socialType 사용자의 소셜 프로바이더 타입
     * @return 인증된 사용자의 아이디
     * @throws NotVerifiedMemberException 인증되지 않은 사용자인 경우에 발생하는 예외
     */
    public Long findVerifiedMemberIdByAuthIdAndSocialType(
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
    public Long findNotVerifiedMemberIdByAuthIdAndSocialType(
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

    public MemberNotificationSliceResponse findNotificationSliceByMemberId(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        final Slice<MemberNotificationDto> notifications = memberRepository.findNotificationSliceByMemberId(
            memberId, size, createdAt);

        return memberMapper.notificationSliceToResponse(
            notifications.getContent(),
            notifications.hasNext()
        );
    }

    @Transactional
    public Long createMemberWithEmailAndPassword(final String email, final String password) {
        final String encodedPassword = passwordEncoder.encode(password);
        final Member member = memberMapper.createMemberWithEmail(email, encodedPassword);

        boolean isDuplicateTag = memberRepository.checkTagDuplication(member.getTag());
        if (isDuplicateTag) {
            log.warn("member tag duplicate - email:{}, tag:{}", member.getEmail(),
                member.getTag());
            member.updateTagLowerCaseSocialType();
            log.warn("member tag update - tag: {}", member.getTag());
        }

        final Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }

    public Long findVerifiedMemberIdByEmailAndPassword(final String email, final String password) {
        final EmailVerifiedCheckDto dto = memberRepository.findEmailVerifiedCheckDtoByEmail(
                email)
            .orElseThrow(MemberNotFoundException::new);

        if (!dto.isVerified()) {
            throw new NotVerifiedMemberException();
        }

        if (isNotMatched(email, password, dto.email(), dto.password())) {
            throw new CredentialsNotMatchedException();
        }

        return dto.memberId();
    }

    private boolean isNotMatched(
        final String inputEmail,
        final String inputPassword,
        final String email,
        final String password
    ) {
        return !inputEmail.equals(email) ||
            password == null ||
            !passwordEncoder.matches(inputPassword, password);
    }

    public CheckEmailDuplicationResponse checkEmailDuplication(final String email) {
        Boolean isDuplicated = memberRepository.checkEmailDuplication(email);

        return new CheckEmailDuplicationResponse(isDuplicated);
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
}
