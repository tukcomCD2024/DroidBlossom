package site.timecapsulearchive.core.domain.friend.repository;

import static site.timecapsulearchive.core.domain.friend.entity.QFriendInvite.friendInvite;
import static site.timecapsulearchive.core.domain.friend.entity.QMemberFriend.memberFriend;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.entity.FriendStatus;

@Repository
@RequiredArgsConstructor
public class MemberFriendQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Slice<FriendSummaryDto> findFriendsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        List<FriendSummaryDto> friends = jpaQueryFactory
            .select(
                Projections.constructor(
                    FriendSummaryDto.class,
                    memberFriend.friend.id,
                    memberFriend.friend.profileUrl,
                    memberFriend.friend.nickname,
                    memberFriend.createdAt
                )
            )
            .from(memberFriend)
            .innerJoin(member).on(memberFriend.owner.id.eq(member.id))
            .innerJoin(member).on(memberFriend.friend.id.eq(member.id))
            .where(memberFriend.owner.id.eq(memberId).and(memberFriend.createdAt.lt(createdAt)))
            .limit(size + 1)
            .fetch();

        final boolean hasNext = canMoreRead(size, friends.size());
        if (hasNext) {
            friends.remove(size);
        }

        return new SliceImpl<>(friends, Pageable.ofSize(size), hasNext);
    }

    private boolean canMoreRead(final int size, final int data) {
        return data > size;
    }

    public Slice<FriendSummaryDto> findFriendRequestsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        List<FriendSummaryDto> friends = jpaQueryFactory
            .select(
                Projections.constructor(
                    FriendSummaryDto.class,
                    friendInvite.friend.id,
                    friendInvite.friend.profileUrl,
                    friendInvite.friend.nickname,
                    friendInvite.createdAt
                )
            )
            .from(friendInvite)
            .innerJoin(member).on(friendInvite.owner.id.eq(member.id))
            .innerJoin(member).on(friendInvite.friend.id.eq(member.id))
            .where(friendInvite.owner.id.eq(memberId)
                .and(friendInvite.createdAt.lt(createdAt))
                .and(friendInvite.friendStatus.eq(FriendStatus.PENDING)))
            .limit(size + 1)
            .fetch();

        final boolean hasNext = canMoreRead(size, friends.size());
        if (hasNext) {
            friends.remove(size);
        }

        return new SliceImpl<>(friends, Pageable.ofSize(size), hasNext);
    }
}
