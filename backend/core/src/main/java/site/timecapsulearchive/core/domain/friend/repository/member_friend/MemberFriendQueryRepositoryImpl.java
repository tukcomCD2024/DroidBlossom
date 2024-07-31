package site.timecapsulearchive.core.domain.friend.repository.member_friend;

import static site.timecapsulearchive.core.domain.friend.entity.QMemberFriend.memberFriend;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDtoByTag;
import site.timecapsulearchive.core.domain.friend.data.request.FriendBeforeGroupInviteRequest;
import site.timecapsulearchive.core.domain.friend.entity.QFriendInvite;
import site.timecapsulearchive.core.global.common.wrapper.ByteArrayWrapper;

@Repository
@RequiredArgsConstructor
public class MemberFriendQueryRepositoryImpl implements MemberFriendQueryRepository {

    private static final double MATCH_THRESHOLD = 0;
    private static final String MATCH_AGAINST_FUNCTION = "function('match_against', {0}, {1})";
    private static final String FRIEND_INVITE_TO_ME_PATH = "friendInviteToMe";
    private static final String FRIEND_INVITE_TO_FRIEND_PATH = "friendInviteToFriend";

    private final JPAQueryFactory jpaQueryFactory;

    public Slice<FriendSummaryDto> findFriendsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        final List<FriendSummaryDto> friends = jpaQueryFactory
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
            .join(member).on(memberFriend.owner.eq(member), member.deletedAt.isNull())
            .join(member).on(memberFriend.friend.eq(member), member.deletedAt.isNull())
            .where(memberFriend.owner.id.eq(memberId).and(memberFriend.createdAt.lt(createdAt)))
            .limit(size + 1)
            .fetch();

        return getFriendSummaryDtos(size, friends);
    }

    private Slice<FriendSummaryDto> getFriendSummaryDtos(final int size,
        final List<FriendSummaryDto> friendSummaryDtos) {
        final boolean hasNext = friendSummaryDtos.size() > size;
        if (hasNext) {
            friendSummaryDtos.remove(size);
        }

        return new SliceImpl<>(friendSummaryDtos, Pageable.ofSize(size), hasNext);
    }

    public Slice<FriendSummaryDto> findFriends(
        final FriendBeforeGroupInviteRequest request
    ) {
        final List<FriendSummaryDto> friends = jpaQueryFactory
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
            .where(memberFriend.owner.id.eq(request.memberId())
                .and(memberFriend.createdAt.lt(request.createdAt()))
            )
            .limit(request.size() + 1)
            .fetch();

        return getFriendSummaryDtos(request.size(), friends);
    }

    public List<SearchFriendSummaryDto> findFriendsByPhone(
        final Long memberId,
        final List<byte[]> hashes
    ) {
        final QFriendInvite friendInviteToFriend = new QFriendInvite(FRIEND_INVITE_TO_FRIEND_PATH);
        final QFriendInvite friendInviteToMe = new QFriendInvite(FRIEND_INVITE_TO_ME_PATH);

        return jpaQueryFactory
            .select(
                Projections.constructor(
                    SearchFriendSummaryDto.class,
                    member.id,
                    member.profileUrl,
                    member.nickname,
                    Projections.constructor(
                        ByteArrayWrapper.class,
                        member.phoneHash
                    ),
                    memberFriend.id.isNotNull(),
                    friendInviteToFriend.id.isNotNull(),
                    friendInviteToMe.id.isNotNull()
                )
            )
            .from(member)
            .leftJoin(memberFriend)
            .on(memberFriend.friend.id.eq(member.id).and(memberFriend.owner.id.eq(memberId)),
                memberFriend.deletedAt.isNull())
            .leftJoin(friendInviteToFriend)
            .on(friendInviteToFriend.friend.id.eq(member.id)
                    .and(friendInviteToFriend.owner.id.eq(memberId)),
                friendInviteToFriend.deletedAt.isNull())
            .leftJoin(friendInviteToMe)
            .on(friendInviteToMe.friend.id.eq(memberId)
                    .and(friendInviteToMe.owner.id.eq(member.id)),
                friendInviteToMe.deletedAt.isNull())
            .where(member.phoneHash.in(hashes).and(member.phoneSearchAvailable.eq(Boolean.TRUE)))
            .fetch();
    }

    public Optional<SearchFriendSummaryDtoByTag> findFriendsByTag(
        final Long memberId,
        final String tag
    ) {
        if (tag != null && tag.isBlank()) {
            return Optional.empty();
        }

        final QFriendInvite friendInviteToFriend = new QFriendInvite(FRIEND_INVITE_TO_FRIEND_PATH);
        final QFriendInvite friendInviteToMe = new QFriendInvite(FRIEND_INVITE_TO_ME_PATH);

        NumberTemplate<Double> tagFullTextSearchTemplate = Expressions.numberTemplate(Double.class,
            MATCH_AGAINST_FUNCTION,
            member.tag,
            tag);

        OrderSpecifier<Boolean> tagFullyMatchFirstOrder = new CaseBuilder().when(member.tag.eq(tag))
            .then(Boolean.TRUE)
            .otherwise(Boolean.FALSE).desc();

        return Optional.ofNullable(jpaQueryFactory
            .select(
                Projections.constructor(
                    SearchFriendSummaryDtoByTag.class,
                    member.id,
                    member.profileUrl,
                    member.nickname,
                    memberFriend.id.isNotNull(),
                    friendInviteToFriend.id.isNotNull(),
                    friendInviteToMe.id.isNotNull()
                )
            )
            .from(member)
            .leftJoin(memberFriend)
            .on(memberFriend.friend.id.eq(member.id).and(memberFriend.owner.id.eq(memberId)),
                memberFriend.deletedAt.isNull())
            .leftJoin(friendInviteToFriend)
            .on(friendInviteToFriend.friend.id.eq(member.id)
                    .and(friendInviteToFriend.owner.id.eq(memberId)),
                friendInviteToFriend.deletedAt.isNull())
            .leftJoin(friendInviteToMe)
            .on(friendInviteToMe.owner.id.eq(member.id)
                    .and(friendInviteToMe.friend.id.eq(memberId)),
                friendInviteToMe.deletedAt.isNull())
            .where(tagFullTextSearchTemplate.gt(MATCH_THRESHOLD)
                .and(member.tagSearchAvailable.eq(Boolean.TRUE)))
            .orderBy(tagFullyMatchFirstOrder, tagFullTextSearchTemplate.desc())
            .limit(1L)
            .fetchOne()
        );
    }

    public List<Long> findFriendIdsByOwnerId(Long memberId) {
        return jpaQueryFactory
            .select(memberFriend.friend.id)
            .from(memberFriend)
            .where(memberFriend.owner.id.eq(memberId))
            .fetch();
    }

    public List<Long> findFriendIds(final List<Long> groupMemberIds, final Long memberId) {
        return jpaQueryFactory.select(memberFriend.friend.id)
            .from(memberFriend)
            .where(
                memberFriend.friend.id.in(groupMemberIds).and(memberFriend.owner.id.eq(memberId)))
            .fetch();
    }
}
