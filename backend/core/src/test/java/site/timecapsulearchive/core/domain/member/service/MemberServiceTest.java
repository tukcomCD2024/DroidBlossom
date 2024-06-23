package site.timecapsulearchive.core.domain.member.service;

import static org.mockito.Mockito.mock;

import site.timecapsulearchive.core.domain.member.data.mapper.MemberMapper;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.domain.member.repository.MemberTemporaryRepository;

class MemberServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final MemberTemporaryRepository memberTemporaryRepository = mock(
        MemberTemporaryRepository.class);
    private final MemberMapper memberMapper = mock(MemberMapper.class);

    private final MemberService memberService = new MemberService(
        memberRepository,
        memberTemporaryRepository,
        memberMapper
    );
}
