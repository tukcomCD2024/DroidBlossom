package site.timecapsulearchive.core.domain.capsule.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.common.fixture.CapsuleFixture;
import site.timecapsulearchive.core.common.fixture.CapsuleSkinFixture;
import site.timecapsulearchive.core.common.fixture.MemberFixture;
import site.timecapsulearchive.core.common.fixture.MemberFriendFixture;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.member.entity.Member;

@FlywayTest
@TestConstructor(autowireMode = AutowireMode.ALL)
class PublicCapsuleQueryRepositoryTest extends RepositoryTest {

    private final PublicCapsuleQueryRepository publicCapsuleQueryRepository;

    private Capsule capsule;
    private Member member;
    private Member notFriend;

    PublicCapsuleQueryRepositoryTest(EntityManager entityManager) {
        this.publicCapsuleQueryRepository = new PublicCapsuleQueryRepository(
            new JPAQueryFactory(entityManager));
    }

    @BeforeEach
    @Transactional
    void setup(@Autowired EntityManager entityManager) {
        member = MemberFixture.member(1);
        entityManager.persist(member);

        Member friend = MemberFixture.member(2);
        entityManager.persist(friend);

        notFriend = MemberFixture.member(3);
        entityManager.persist(notFriend);

        MemberFriend ownerFriend = MemberFriendFixture.memberFriend(member, friend);
        MemberFriend friendOwner = MemberFriendFixture.memberFriend(friend, member);
        entityManager.persist(ownerFriend);
        entityManager.persist(friendOwner);

        CapsuleSkin capsuleSkin = CapsuleSkinFixture.capsuleSkin(friend);
        entityManager.persist(capsuleSkin);

        capsule = CapsuleFixture.publicCapsule(friend, capsuleSkin);
        entityManager.persist(capsule);
    }

    @Test
    void 특정_사용자의_친구_캡슐을_상세_조회하면_친구_캡슐_상세_내용을_볼_수_있다() {
        //given
        Long memberId = member.getId();
        Long capsuleId = capsule.getId();

        //when
        Optional<CapsuleDetailDto> detailDto = publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
            memberId, capsuleId);

        //then
        assertThat(detailDto.isPresent()).isTrue();
    }

    @Test
    void 친구가_아닌_사용자가_친구_캡슐을_상세_조회하면_친구_캡슐_상세_내용을_볼_수_없다() {
        //given
        Long memberId = notFriend.getId();
        Long capsuleId = capsule.getId();

        //when
        Optional<CapsuleDetailDto> detailDto = publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
            memberId, capsuleId);

        //then
        assertThat(detailDto.isEmpty()).isTrue();
    }

    @Test
    void 특정_사용자의_친구_캡슐을_요약_조회하면_친구_캡슐_요약_내용을_볼_수_있다() {
        //given
        Long memberId = member.getId();
        Long capsuleId = capsule.getId();

        //when
        Optional<CapsuleSummaryDto> detailDto = publicCapsuleQueryRepository.findPublicCapsuleSummaryDtosByMemberIdAndCapsuleId(
            memberId, capsuleId);

        //then
        assertThat(detailDto.isPresent()).isTrue();
    }

    @Test
    void 친구가_아닌_사용자가_친구_캡슐을_요약_조회하면_친구_캡슐_요약_내용을_볼_수_없다() {
        //given
        Long memberId = notFriend.getId();
        Long capsuleId = capsule.getId();

        //when
        Optional<CapsuleSummaryDto> detailDto = publicCapsuleQueryRepository.findPublicCapsuleSummaryDtosByMemberIdAndCapsuleId(
            memberId, capsuleId);

        //then
        assertThat(detailDto.isEmpty()).isTrue();
    }
}