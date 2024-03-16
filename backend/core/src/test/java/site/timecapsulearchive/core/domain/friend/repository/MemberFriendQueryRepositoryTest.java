package site.timecapsulearchive.core.domain.friend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendSummaryDto;

@TestConstructor(autowireMode = AutowireMode.ALL)
class MemberFriendQueryRepositoryTest extends RepositoryTest {

    private final MemberFriendQueryRepository memberFriendQueryRepository;

    MemberFriendQueryRepositoryTest(EntityManager entityManager) {
        this.memberFriendQueryRepository = new MemberFriendQueryRepository(
            new JPAQueryFactory(entityManager));
    }

    @DisplayName("사용자의_친구_목록_조회_테스트")
    @Test
    void findFriendsSlice() {
        //given
        Long memberId = 21L;
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendsSlice(
            memberId,
            size,
            now
        );

        //then
        assertThat(slice.getContent().size()).isEqualTo(size);
    }

    @DisplayName("사용자의_친구_목록_조회_테스트")
    @ParameterizedTest
    @ValueSource(ints = {20, 15, 10, 5})
    void findFriendsSliceWithDifferentSize(int size) {
        //given
        Long memberId = 21L;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendsSlice(
            memberId,
            size,
            now
        );

        //then
        assertThat(slice.getContent().size()).isEqualTo(size);
    }

    @DisplayName("유효하지_않은_시간으로_사용자의_친구_목록_조회_테스트")
    @Test
    void findFriendsSliceWithNotValidDate() {
        //given
        Long memberId = 21L;
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendsSlice(
            memberId,
            size,
            now
        );

        //then
        assertThat(slice.getContent().size()).isEqualTo(0);
    }

    @DisplayName("친구가_없는_사용자의_친구_목록_조회_테스트")
    @Test
    void findFriendsSliceWithNotMemberId() {
        //given
        Long memberId = 20L;
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendsSlice(
            memberId,
            size,
            now
        );

        //then
        assertThat(slice.getContent().size()).isEqualTo(0);
    }

    @DisplayName("사용자의_친구_요청_목록_조회_테스트")
    @Test
    void findFriendRequestsSlice() {
        //given
        Long memberId = 21L;
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendRequestsSlice(
            memberId,
            size,
            now
        );

        //then
        assertThat(slice.getContent().size()).isEqualTo(size);
    }

    @DisplayName("사용자의_친구_요청_목록_조회_테스트")
    @ParameterizedTest
    @ValueSource(ints = {20, 15, 10, 5})
    void findFriendRequestsSliceWithDifferentSize(int size) {
        //given
        Long memberId = 21L;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendRequestsSlice(
            memberId,
            size,
            now
        );

        //then
        assertThat(slice.getContent().size()).isEqualTo(size);
    }

    @DisplayName("유효하지_않은_시간으로_사용자의_친구_요청_목록_조회_테스트")
    @Test
    void findFriendRequestsSliceWithNotValidDate() {
        //given
        Long memberId = 21L;
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendRequestsSlice(
            memberId,
            size,
            now
        );

        //then
        assertThat(slice.getContent().size()).isEqualTo(0);
    }

    @DisplayName("친구가_없는_사용자의_친구_요청_목록_조회_테스트")
    @Test
    void findFriendRequestsSliceWithNotMemberId() {
        //given
        Long memberId = 20L;
        int size = 20;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(5);

        //when
        Slice<FriendSummaryDto> slice = memberFriendQueryRepository.findFriendRequestsSlice(
            memberId,
            size,
            now
        );

        //then
        assertThat(slice.getContent().size()).isEqualTo(0);
    }
}