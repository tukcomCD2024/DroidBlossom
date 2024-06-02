package site.timecapsulearchive.core.domain.friend.repository.friend_invite;

import static site.timecapsulearchive.core.domain.friend.entity.QFriendInvite.friendInvite;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendInviteMemberIdsDto;

@Repository
@RequiredArgsConstructor
public class FriendInviteQueryRepositoryImpl implements FriendInviteQueryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory jpaQueryFactory;

    public void bulkSave(final Long ownerId, final List<Long> friendIds) {
        if (friendIds.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
            """
                INSERT INTO friend_invite (
                friend_invite_id, owner_id, friend_id, created_at, updated_at
                ) values (?, ?, ?, ?, ?)
                """,
            new BatchPreparedStatementSetter() {

                @Override
                public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                    final Long friendId = friendIds.get(i);
                    ps.setNull(1, Types.BIGINT);
                    ps.setLong(2, ownerId);
                    ps.setLong(3, friendId);
                    ps.setTimestamp(4, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
                    ps.setTimestamp(5, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
                }

                @Override
                public int getBatchSize() {
                    return friendIds.size();
                }
            }
        );
    }

    @Override
    public List<FriendInviteMemberIdsDto> findFriendInviteMemberIdsDtoByMemberIdsAndFriendId(
        List<Long> memberIds, Long friendId) {
        BooleanBuilder multipleColumnsInCondition = new BooleanBuilder();
        for (Long memberId : memberIds) {
            multipleColumnsInCondition.or(friendInvite.owner.id.eq(memberId)
                .and(friendInvite.friend.id.eq(friendId)));

            multipleColumnsInCondition.or(friendInvite.owner.id.eq(friendId)
                .and(friendInvite.friend.id.eq(memberId)));
        }

        return jpaQueryFactory
            .select(
                Projections.constructor(
                    FriendInviteMemberIdsDto.class,
                    friendInvite.owner.id,
                    friendInvite.friend.id
                )
            )
            .from(friendInvite)
            .where(multipleColumnsInCondition)
            .fetch();
    }

    @Override
    public Optional<FriendInviteMemberIdsDto> findFriendInviteMemberIdsDtoByMemberIdAndFriendId(
        Long memberId, Long friendId) {
        return Optional.ofNullable(
            jpaQueryFactory
                .select(
                    Projections.constructor(
                        FriendInviteMemberIdsDto.class,
                        friendInvite.owner.id,
                        friendInvite.friend.id
                    )
                )
                .from(friendInvite)
                .where(friendInvite.owner.id.eq(memberId).and(friendInvite.friend.id.eq(friendId))
                    .or(friendInvite.owner.id.eq(friendId)
                        .and(friendInvite.friend.id.eq(memberId))))
                .fetchOne()
        );
    }
}
