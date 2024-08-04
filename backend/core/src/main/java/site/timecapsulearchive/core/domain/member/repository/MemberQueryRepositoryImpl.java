package site.timecapsulearchive.core.domain.member.repository;

import static site.timecapsulearchive.core.domain.friend.entity.QMemberFriend.memberFriend;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;
import static site.timecapsulearchive.core.domain.member_group.entity.QMemberGroup.memberGroup;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.member.data.dto.MemberDetailDto;
import site.timecapsulearchive.core.domain.member.data.dto.MemberStatusDto;
import site.timecapsulearchive.core.domain.member.data.dto.VerifiedCheckDto;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.global.common.wrapper.ByteArrayWrapper;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepositoryImpl implements MemberQueryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public MemberStatusDto findIsVerifiedByAuthIdAndSocialType(
        final String authId,
        final SocialType socialType
    ) {
        try {
            return jdbcTemplate.queryForObject(
                "select m.is_verified, m.deleted_at is not null as deleted_at "
                    + "from member m "
                    + "where m.auth_id = ? and m.social_type = ?",
                (rs, rowNum) -> MemberStatusDto.exist(rs.getBoolean("is_verified"),
                    rs.getBoolean("deleted_at")),
                authId, socialType.name()
            );
        } catch (EmptyResultDataAccessException e) {
            return MemberStatusDto.notExist();
        }
    }

    @Override
    public Optional<VerifiedCheckDto> findVerifiedCheckDtoByAuthIdAndSocialType(
        final String authId,
        final SocialType socialType
    ) {
        return Optional.ofNullable(
            jpaQueryFactory
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

    @Override
    public Optional<MemberDetailDto> findMemberDetailResponseDtoById(final Long memberId) {
        return Optional.ofNullable(
            jpaQueryFactory
                .select(
                    Projections.constructor(
                        MemberDetailDto.class,
                        member.nickname,
                        member.profileUrl,
                        member.tag,
                        member.socialType,
                        member.email,
                        Projections.constructor(
                            ByteArrayWrapper.class,
                            member.phone
                        ),
                        countDistinct(memberFriend.id),
                        countDistinct(memberGroup.id),
                        member.tagSearchAvailable,
                        member.phoneSearchAvailable
                    )
                )
                .from(member)
                .leftJoin(memberFriend).on(member.eq(memberFriend.owner),
                    memberFriend.deletedAt.isNull())
                .leftJoin(memberGroup).on(member.eq(memberGroup.member),
                    memberGroup.deletedAt.isNull())
                .where(member.id.eq(memberId))
                .groupBy(member.id)
                .fetchOne()
        );
    }

    private NumberExpression<Long> countDistinct(final NumberExpression<Long> expression) {
        return Expressions.numberTemplate(Long.class, "COUNT(DISTINCT {0})", expression);
    }

    @Override
    public Optional<Boolean> findIsAlarmByMemberId(final Long memberId) {
        return Optional.ofNullable(
            jpaQueryFactory.select(member.notificationEnabled)
                .from(member)
                .where(member.id.eq(memberId))
                .fetchOne()
        );
    }

    @Override
    public List<Long> findMemberIdsByIds(final List<Long> ids) {
        return jpaQueryFactory
            .select(member.id)
            .from(member)
            .where(member.id.in(ids))
            .fetch();
    }

    @Override
    public boolean checkTagDuplication(final String tag) {
        final Integer count = jpaQueryFactory.selectOne()
            .from(member)
            .where(member.tag.eq(tag))
            .fetchFirst();

        return count != null;
    }

    @Override
    public Optional<ByteArrayWrapper> findMemberPhoneHash(final Long memberId) {
        final byte[] phoneHash = jpaQueryFactory
            .select(
                member.phoneHash
            )
            .from(member)
            .where(member.id.eq(memberId))
            .fetchOne();

        if (phoneHash != null) {
            return Optional.of(new ByteArrayWrapper(phoneHash));
        }

        return Optional.empty();
    }
}
