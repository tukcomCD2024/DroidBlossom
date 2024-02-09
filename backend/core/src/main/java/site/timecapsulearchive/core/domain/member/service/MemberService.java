package site.timecapsulearchive.core.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.member.data.dto.MemberDetailResponseDto;
import site.timecapsulearchive.core.domain.member.data.dto.SignUpRequestDto;
import site.timecapsulearchive.core.domain.member.data.dto.VerifiedCheckDto;
import site.timecapsulearchive.core.domain.member.data.mapper.MemberMapper;
import site.timecapsulearchive.core.domain.member.data.response.MemberDetailResponse;
import site.timecapsulearchive.core.domain.member.data.response.MemberStatusResponse;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.domain.member.exception.AlreadyVerifiedException;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.exception.NotVerifiedMemberException;
import site.timecapsulearchive.core.domain.member.repository.MemberQueryRepository;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.global.security.encryption.AESEncryptionManager;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final AESEncryptionManager aesEncryptionManager;

    private final MemberMapper memberMapper;

    @Transactional
    public Long createMember(final SignUpRequestDto dto) {
        final Member member = memberMapper.signUpRequestDtoToEntity(dto);

        final Member savedMember = memberRepository.save(member);

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

        final Boolean isVerified = memberQueryRepository.findIsVerifiedByAuthIdAndSocialType(
            authId,
            socialType
        );

        if (isVerified == null) {
            return MemberStatusResponse.empty();
        }

        return MemberStatusResponse.from(isVerified);
    }

    public Member findMemberByMemberId(final Long memberId) {
        return memberRepository.findMemberById(memberId)
            .orElseThrow(MemberNotFoundException::new);
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
        final VerifiedCheckDto dto = memberQueryRepository.findVerifiedCheckDtoByAuthIdAndSocialType(
                authId, socialType)
            .orElseThrow(MemberNotFoundException::new);

        if (isNotVerified(dto)) {
            throw new NotVerifiedMemberException();
        }

        return dto.memberId();
    }

    private boolean isNotVerified(final VerifiedCheckDto dto) {
        return !dto.isVerified();
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
    ) throws AlreadyVerifiedException {
        final VerifiedCheckDto dto = memberQueryRepository.findVerifiedCheckDtoByAuthIdAndSocialType(
                authId, socialType)
            .orElseThrow(MemberNotFoundException::new);

        if (isVerified(dto)) {
            throw new AlreadyVerifiedException();
        }

        return dto.memberId();
    }

    private boolean isVerified(final VerifiedCheckDto dto) {
        return dto.isVerified();
    }

    public MemberDetailResponse findMemberDetailById(final Long memberId) {
        final MemberDetailResponseDto dto = memberQueryRepository.findMemberDetailResponseDtoById(
                memberId)
            .orElseThrow(MemberNotFoundException::new);

        final String decryptedPhone = aesEncryptionManager.decryptWithPrefixIV(dto.phone());

        return memberMapper.memberDetailResponseDtoToResponse(dto, decryptedPhone);
    }
}
