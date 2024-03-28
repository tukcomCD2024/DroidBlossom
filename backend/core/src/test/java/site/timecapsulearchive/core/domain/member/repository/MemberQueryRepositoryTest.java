package site.timecapsulearchive.core.domain.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.SocialType;

@FlywayTest
@TestConstructor(autowireMode = AutowireMode.ALL)
class MemberQueryRepositoryTest extends RepositoryTest {

    private final MemberQueryRepository memberQueryRepository;

    MemberQueryRepositoryTest(EntityManager entityManager) {
        this.memberQueryRepository = new MemberQueryRepository(
            new JPAQueryFactory(entityManager));
    }

    @Transactional
    @BeforeEach
    void setup(@Autowired EntityManager entityManager) {
        Member owner = getMember();
        entityManager.persist(owner);
    }

    private Member getMember() {
        return Member.builder()
            .socialType(SocialType.GOOGLE)
            .nickname("testNickname")
            .email("test@google.com")
            .authId("test")
            .profileUrl("test.com")
            .tag("testTag")
            .build();
    }

    @Test
    void 중복_이메일로_중복_체크하면_True가_반환된다() {
        //given
        String duplicatedEmail = "test@google.com";

        //when
        Boolean isDuplicated = memberQueryRepository.checkEmailDuplication(duplicatedEmail);

        //then
        assertThat(isDuplicated).isTrue();
    }

    @Test
    void 고유한_이메일로_중복_체크_테스트하면_False가_반환된다() {
        //given
        String uniqueEmail = "unique@google.com";

        //when
        Boolean isDuplicated = memberQueryRepository.checkEmailDuplication(uniqueEmail);

        //then
        assertThat(isDuplicated).isFalse();
    }
}