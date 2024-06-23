package site.timecapsulearchive.core.domain.member.service;

import static org.mockito.Mockito.mock;

import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.domain.member.repository.MemberTemporaryRepository;

class MemberServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final MemberTemporaryRepository memberTemporaryRepository = mock(
        MemberTemporaryRepository.class);

    private final MemberService memberService = new MemberService(
        memberRepository,
        memberTemporaryRepository
    );
}
