package site.timecapsulearchive.core.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.auth.entity.SocialType;
import site.timecapsulearchive.core.domain.member.dto.response.MemberStatusResponse;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.repository.MemberQueryRepository;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;

    @Transactional
    public Long createMember(Member member) {
        Member savedMember = memberRepository.save(member);

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
        String authId,
        SocialType socialType
    ) {
        Boolean isVerified = memberQueryRepository.findIsVerifiedByAuthIdAndSocialType(
            authId,
            socialType
        );

        if (isVerified == null) {
            return MemberStatusResponse.empty();
        }

        return MemberStatusResponse.from(isVerified);
    }
}
