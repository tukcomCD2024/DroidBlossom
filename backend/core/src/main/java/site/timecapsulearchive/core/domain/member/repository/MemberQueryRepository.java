package site.timecapsulearchive.core.domain.member.repository;

import static site.timecapsulearchive.core.domain.friend.entity.QMemberFriend.memberFriend;
import static site.timecapsulearchive.core.domain.member_group.entity.QMemberGroup.memberGroup;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;
import static site.timecapsulearchive.core.domain.member.entity.QNotification.notification;
import static site.timecapsulearchive.core.domain.member.entity.QNotificationCategory.notificationCategory;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.member.data.dto.EmailVerifiedCheckDto;
import site.timecapsulearchive.core.domain.member.data.dto.MemberDetailDto;
import site.timecapsulearchive.core.domain.member.data.dto.MemberNotificationDto;
import site.timecapsulearchive.core.domain.member.data.dto.VerifiedCheckDto;
import site.timecapsulearchive.core.domain.member.entity.SocialType;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final JPAQueryFactory query;

    public Boolean findIsVerifiedByAuthIdAndSocialType(
        final String authId,
        final SocialType socialType
    ) {
        return query.select(member.isVerified)
            .from(member)
            .where(member.authId.eq(authId).and(member.socialType.eq(socialType)))
            .fetchOne();
    }

    public Optional<VerifiedCheckDto> findVerifiedCheckDtoByAuthIdAndSocialType(
        final String authId,
        final SocialType socialType
    ) {
        return Optional.ofNullable(
            query
                .select(
                    Projections.constructor(
                        VerifiedCheckDto.class,
                        member.id,
                        member.isVerified
                    )
                )
                .from(member)
                .where(member.authId.eq(authId).and(member.socialType.eq(socialType)))
                .fetchOne()
        );
    }

    public Optional<MemberDetailDto> findMemberDetailResponseDtoById(final Long memberId) {
        return Optional.ofNullable(
            query
                .select(
                    Projections.constructor(
                        MemberDetailDto.class,
                        member.nickname,
                        member.profileUrl,
                        member.tag,
                        countDistinct(memberFriend.id),
                        countDistinct(memberGroup.id)
                    )
                )
                .from(member)
                .leftJoin(memberFriend).on(member.id.eq(memberFriend.owner.id))
                .leftJoin(memberGroup).on(member.id.eq(memberGroup.member.id))
                .where(member.id.eq(memberId))
                .groupBy(member.id)
                .fetchOne()
        );
    }

    private NumberExpression<Long> countDistinct(final NumberExpression<Long> expression) {
        return Expressions.numberTemplate(Long.class, "COUNT(DISTINCT {0})", expression);
    }

    public Slice<MemberNotificationDto> findNotificationSliceByMemberId(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        final List<MemberNotificationDto> notifications = findMemberNotificationDtos(
            memberId, size, createdAt);

        final boolean hasNext = notifications.size() > size;
        if (hasNext) {
            notifications.remove(size);
        }

        return new SliceImpl<>(notifications, Pageable.ofSize(size), hasNext);
    }

    private List<MemberNotificationDto> findMemberNotificationDtos(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return query
            .select(
                Projections.constructor(
                    MemberNotificationDto.class,
                    notification.title,
                    notification.text,
                    notification.createdAt,
                    notification.imageUrl,
                    notificationCategory.categoryName,
                    notification.status
                )
            )
            .from(notification)
            .join(notification.notificationCategory, notificationCategory)
            .where(notification.createdAt.lt(createdAt).and(notification.member.id.eq(memberId)))
            .orderBy(notification.id.desc())
            .limit(size + 1)
            .fetch();
    }

    public Optional<EmailVerifiedCheckDto> findEmailVerifiedCheckDtoByEmail(
        final String email
    ) {
        return Optional.ofNullable(
            query
                .select(
                    Projections.constructor(
                        EmailVerifiedCheckDto.class,
                        member.id,
                        member.isVerified,
                        member.email,
                        member.password
                    )
                )
                .from(member)
                .where(member.email.eq(email))
                .fetchOne()
        );
    }

    public Boolean checkEmailDuplication(final String email) {
        final Integer count = query.selectOne()
            .from(member)
            .where(member.email.eq(email))
            .fetchFirst();

        return count != null;
    }

    public Optional<Boolean> findIsAlarmByMemberId(final Long memberId) {
        return Optional.ofNullable(
            query.select(member.notificationEnabled)
                .from(member)
                .where(member.id.eq(memberId))
                .fetchOne()
        );
    }

    public List<Long> findMemberIdsByIds(List<Long> ids) {
        return query
            .select(member.id)
            .from(member)
            .where(member.id.in(ids))
            .fetch();
    }
}
