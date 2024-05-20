package site.timecapsulearchive.core.domain.capsule.public_capsule.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleFixture;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleSkinFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFriendFixture;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto.MyPublicCapsuleDto;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto.PublicCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.member.entity.Member;

@TestConstructor(autowireMode = AutowireMode.ALL)
class PublicCapsuleQueryRepositoryTest extends RepositoryTest {

    private final PublicCapsuleQueryRepository publicCapsuleQueryRepository;

    private Long friendCapsuleId;
    private Long myCapsuleId;
    private Long memberId;
    private Long friendId;
    private Long notFriendId;

    PublicCapsuleQueryRepositoryTest(EntityManager entityManager) {
        this.publicCapsuleQueryRepository = new PublicCapsuleQueryRepository(
            new JPAQueryFactory(entityManager));
    }

    @BeforeEach
    @Transactional
    void setup(@Autowired EntityManager entityManager) {
        //사용자
        Member member = MemberFixture.member(1);
        entityManager.persist(member);
        memberId = member.getId();

        //친구
        Member friend = MemberFixture.member(2);
        entityManager.persist(friend);
        friendId = friend.getId();

        //친구 아닌 사용자
        Member notFriend = MemberFixture.member(3);
        entityManager.persist(notFriend);
        notFriendId = notFriend.getId();

        //사용자 <-> 친구 관계
        MemberFriend ownerFriend = MemberFriendFixture.memberFriend(member, friend);
        MemberFriend friendOwner = MemberFriendFixture.memberFriend(friend, member);
        entityManager.persist(ownerFriend);
        entityManager.persist(friendOwner);

        //사용자 캡슐 스킨 & 캡슐
        CapsuleSkin myCapsuleSkin = CapsuleSkinFixture.capsuleSkin(member);
        entityManager.persist(myCapsuleSkin);

        Capsule myCapsule = CapsuleFixture.capsule(member, myCapsuleSkin, CapsuleType.PUBLIC);
        entityManager.persist(myCapsule);
        myCapsuleId = myCapsule.getId();

        //사용자의 캡슐들 생성
        CapsuleFixture.capsules(10, member, myCapsuleSkin, CapsuleType.PUBLIC)
            .forEach(entityManager::persist);

        //친구의 캡슐 스킨 & 캡슐
        CapsuleSkin friendCapsuleSkin = CapsuleSkinFixture.capsuleSkin(friend);
        entityManager.persist(friendCapsuleSkin);

        Capsule friendCapsule = CapsuleFixture.capsule(friend, friendCapsuleSkin,
            CapsuleType.PUBLIC);
        entityManager.persist(friendCapsule);
        friendCapsuleId = friendCapsule.getId();

        //친구의 캡슐들 생성
        CapsuleFixture.capsules(10, friend, friendCapsuleSkin, CapsuleType.PUBLIC)
            .forEach(entityManager::persist);

        //사용자의 친구가 아닌 사용자의 캡슐 스킨 & 캡슐
        CapsuleSkin notFriendCapsuleSkin = CapsuleSkinFixture.capsuleSkin(notFriend);
        entityManager.persist(notFriendCapsuleSkin);

        Capsule capsuleByNotFriend = CapsuleFixture.capsule(notFriend, notFriendCapsuleSkin,
            CapsuleType.PUBLIC);
        entityManager.persist(capsuleByNotFriend);
    }

    @Test
    void 친구가_공개_캡슐을_상세_조회하면_공개_캡슐_상세_내용을_볼_수_있다() {
        //given
        //when
        Optional<CapsuleDetailDto> detailDto = publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
            friendId, myCapsuleId);

        //then
        assertThat(detailDto.isPresent()).isTrue();
    }

    @Test
    void 친구가_아닌_사용자가_친구_캡슐을_상세_조회하면_친구_캡슐_상세_내용을_볼_수_없다() {
        //given
        //when
        Optional<CapsuleDetailDto> detailDto = publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
            notFriendId, friendCapsuleId);

        //then
        assertThat(detailDto.isEmpty()).isTrue();
    }

    @Test
    void 사용자가_만든_공개_캡슐을_상세_조회하면_공개_캡슐_상세_내용을_볼_수_있다() {
        //given
        //when
        Optional<CapsuleDetailDto> detailDto = publicCapsuleQueryRepository.findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
            memberId, myCapsuleId);

        //then
        assertThat(detailDto.isPresent()).isTrue();
    }

    @Test
    void 친구가_공개_캡슐을_요약_조회하면_공개_캡슐_요약_내용을_볼_수_있다() {
        //given
        //when
        Optional<CapsuleSummaryDto> detailDto = publicCapsuleQueryRepository.findPublicCapsuleSummaryDtosByMemberIdAndCapsuleId(
            friendId, friendCapsuleId);

        //then
        assertThat(detailDto.isPresent()).isTrue();
    }

    @Test
    void 친구가_아닌_사용자가_친구_캡슐을_요약_조회하면_친구_캡슐_요약_내용을_볼_수_없다() {
        //given
        //when
        Optional<CapsuleSummaryDto> detailDto = publicCapsuleQueryRepository.findPublicCapsuleSummaryDtosByMemberIdAndCapsuleId(
            notFriendId, friendCapsuleId);

        //then
        assertThat(detailDto.isEmpty()).isTrue();
    }

    @Test
    void 사용자는_나_혹은_친구가_생성한_공개_캡슐을_조회할_수_있다() {
        //given
        int size = 3;
        ZonedDateTime now = ZonedDateTime.now().plusDays(1);

        //when
        Slice<PublicCapsuleDetailDto> dto = publicCapsuleQueryRepository.findPublicCapsulesDtoMadeByFriend(
            memberId, size, now);

        //then
        assertThat(dto.hasContent()).isTrue();
    }

    @Test
    void 존재하지_않는_사용자는_공개_캡슐을_조회하면_빈_리스트를_반환한다() {
        //given
        int size = 3;
        ZonedDateTime now = ZonedDateTime.now().plusDays(1);

        //when
        Slice<PublicCapsuleDetailDto> dto = publicCapsuleQueryRepository.findPublicCapsulesDtoMadeByFriend(
            0L, size, now);

        //then
        assertThat(dto.isEmpty()).isTrue();
    }

    @Test
    void 사용자가_사용자가_만든_공개_캡슐을_조회하면_사용자가_만든_공개_캡슐만_나온다() {
        //given
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now().plusDays(1);

        //when
        Slice<MyPublicCapsuleDto> publicCapsules = publicCapsuleQueryRepository.findMyPublicCapsuleSlice(
            memberId, size, now);

        //then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(publicCapsules.hasContent()).isTrue();
            assertThat(publicCapsules).allMatch(
                capsule -> capsule.capsuleType().equals(CapsuleType.PUBLIC));
            assertThat(publicCapsules).allMatch(capsule -> capsule.createdAt().isBefore(now));
        });
    }
}