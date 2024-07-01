package site.timecapsulearchive.core.domain.capsule.treasure_capsule.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.common.dependency.TestTransactionTemplate;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleFixture;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleSkinFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.capsule.CapsuleRepository;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.capsuleskin.repository.CapsuleSkinRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.global.error.ErrorCode;

class TreasureCapsuleServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final CapsuleRepository capsuleRepository = mock(CapsuleRepository.class);
    private final CapsuleSkinRepository capsuleSkinRepository = mock(CapsuleSkinRepository.class);
    private final TransactionTemplate transactionTemplate = TestTransactionTemplate.spied();

    TreasureCapsuleService treasureCapsuleService = new TreasureCapsuleService(
        memberRepository, capsuleRepository, capsuleSkinRepository, transactionTemplate);


    @Test
    void 보물_캡슐_개봉_시_이미_개봉되었다면_예외가_발생한다() {
        Member member = MemberFixture.member(0);
        CapsuleSkin capsuleSkin = CapsuleSkinFixture.capsuleSkin(member);
        CapsuleType capsuleType = CapsuleType.TREASURE;
        Capsule capsule = CapsuleFixture.capsule(member, capsuleSkin, capsuleType);

        Long memberId = 1L;
        Long capsuleId = 1L;

        given(memberRepository.findMemberById(memberId)).willReturn(Optional.of(member));
        given(capsuleRepository.findCapsuleWithImageByCapsuleId(capsuleId))
            .willThrow(new CapsuleNotFondException());

        assertThatThrownBy(() -> treasureCapsuleService.openTreasureCapsule(memberId, capsuleId))
            .isInstanceOf(CapsuleNotFondException.class)
            .hasMessage(ErrorCode.CAPSULE_NOT_FOUND_ERROR.getMessage());
    }

}
