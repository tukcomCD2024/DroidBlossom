package site.timecapsulearchive.core.domain.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFriendFixture;
import site.timecapsulearchive.core.common.fixture.domain.NotificationFixture;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.member.data.dto.MemberDetailDto;
import site.timecapsulearchive.core.domain.member.entity.Member;

@TestConstructor(autowireMode = AutowireMode.ALL)
class MemberQueryRepositoryTest extends RepositoryTest {

    private final MemberQueryRepository memberQueryRepository;

    private Member member;
    private Member zeroNotificationMember;
    private int friendCount;

    MemberQueryRepositoryTest(EntityManager entityManager) {
        this.memberQueryRepository = new MemberQueryRepositoryImpl(
            new JPAQueryFactory(entityManager));
    }

    @Transactional
    @BeforeEach
    void setup(@Autowired EntityManager entityManager) {
        member = MemberFixture.member(0);
        entityManager.persist(member);

        zeroNotificationMember = MemberFixture.member(1);
        entityManager.persist(zeroNotificationMember);

        for (int count = 2; count < 22; count++) {
            Member friend = MemberFixture.member(count);
            entityManager.persist(friend);

            MemberFriend memberFriend = MemberFriendFixture.memberFriend(member, friend);
            MemberFriend friendMember = MemberFriendFixture.memberFriend(friend, member);

            entityManager.persist(friendMember);
            entityManager.persist(memberFriend);

            friendCount += 1;
        }
    }

    @Test
    void 회원의_아이디로_회원의_상세정보를_조회하면_회원의_상세정보가_반환된다() {
        //given
        //when
        MemberDetailDto detailDto = memberQueryRepository.findMemberDetailResponseDtoById(
                member.getId())
            .orElseThrow();

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(detailDto.tag()).isNotBlank();
            softly.assertThat(detailDto.nickname()).isNotBlank();
            softly.assertThat(detailDto.profileUrl()).isNotBlank();
            softly.assertThat(detailDto.friendCount()).isNotNull();
            softly.assertThat(detailDto.groupCount()).isNotNull();
        });
    }

    @Test
    void 존재하지_않는_회원의_아이디로_회원의_상세정보를_조회하면_빈_값이_반환된다() {
        //given
        Long notExistMemberId = 999999L;

        //when
        Optional<MemberDetailDto> detailDto = memberQueryRepository.findMemberDetailResponseDtoById(
            notExistMemberId);

        //then
        assertThat(detailDto.isEmpty()).isTrue();
    }

    @Test
    void 친구가_존재하는_회원의_아이디로_회원의_상세정보를_조회하면_회원의_친구수가_반환된다() {
        //given
        //when
        MemberDetailDto detailDto = memberQueryRepository.findMemberDetailResponseDtoById(
                member.getId())
            .orElseThrow();

        //then
        assertThat(detailDto.friendCount()).isEqualTo(friendCount);
    }

    @Test
    void 친구가_없는_회원의_아이디로_회원의_상세정보를_조회하면_회원의_친구수인_0이_반환된다() {
        //given
        //when
        MemberDetailDto detailDto = memberQueryRepository.findMemberDetailResponseDtoById(
                zeroNotificationMember.getId())
            .orElseThrow();

        //then
        assertThat(detailDto.friendCount()).isEqualTo(0);
    }
}